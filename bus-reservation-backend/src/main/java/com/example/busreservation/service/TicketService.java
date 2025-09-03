package com.example.busreservation.service;

import java.io.ByteArrayOutputStream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.busreservation.entity.Booking;
import com.example.busreservation.entity.BookingStatus;
import com.example.busreservation.repository.BookingRepository;
import com.example.busreservation.util.QrUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.mail.internet.MimeMessage;

@Service
public class TicketService {
    private final BookingRepository bookingRepo;
    private final QrUtil qr;
    private final JavaMailSender mailSender;

    public TicketService(BookingRepository bookingRepo, QrUtil qr, JavaMailSender mailSender) {
        this.bookingRepo = bookingRepo;
        this.qr = qr;
        this.mailSender = mailSender;
    }

    // ✅ Generate ticket as PDF
    public byte[] generateTicketPdf(Long bookingId) throws Exception {
        Booking b = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (b.getStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException("Ticket not confirmed");
        }

        String text = "PNR:" + b.getPnr() + "; Trip:" + b.getTrip().getId() + "; Seats:" + b.getSeats().size();
        byte[] qrBytes = qr.png(text);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        document.add(new Paragraph("Bus Ticket"));
        document.add(new Paragraph("PNR: " + b.getPnr()));
        document.add(new Paragraph("User: "  + b.getUser().getName()));
//        document.add(new Paragraph("Email: "  + b.getUser().getName()));
        
        document.add(new Paragraph("Trip: " + b.getTrip().getRoute()));
//         document.add(new Paragraph("Trip: " + b.getTrip()));
        document.add(new Paragraph("Departure: " + b.getTrip().getDepartureTime().toString()));
//        document.add(new Paragraph("Seats: " + b.getSeats()));
        document.add(new Paragraph("Amount: " + b.getTotalAmount()));
        document.add(new Paragraph("\nScan QR for verification:"));

        Image qrImage = Image.getInstance(qrBytes);
        qrImage.scaleToFit(150, 150);
        document.add(qrImage);

        document.close();
        return out.toByteArray();
    }

    // ✅ Email ticket as PDF
    public void emailTicket(Long bookingId) throws Exception {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        byte[] pdfBytes = generateTicketPdf(bookingId);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        helper.setFrom("namratha5858@gmail.com");

        helper.setTo(booking.getUser().getEmail());
        helper.setSubject("Your Bus Ticket - PNR " + booking.getPnr());
        helper.setText("Dear " + booking.getUser().getName() + ",\n\nPlease find attached your bus ticket.\n\nSafe journey!");

        helper.addAttachment("ticket-" + booking.getId() + ".pdf", new ByteArrayResource(pdfBytes));

        mailSender.send(message);
    }
}
