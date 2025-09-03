package com.example.busreservation.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.busreservation.entity.Booking;
import com.example.busreservation.entity.BookingStatus;
import com.example.busreservation.repository.BookingRepository;
import com.example.busreservation.service.TicketService;
import com.example.busreservation.util.QrUtil;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final BookingRepository bookingRepo;
    private final QrUtil qr;
    private final TicketService ticketService;

    public TicketController(BookingRepository bookingRepo, QrUtil qr, TicketService ticketService) {
        this.bookingRepo = bookingRepo;
        this.qr = qr;
        this.ticketService = ticketService;
    }

    // ✅ Existing QR endpoint
    @GetMapping("/{bookingId}/qr")
    public ResponseEntity<byte[]> qr(@PathVariable Long bookingId) throws Exception {
        Booking b = bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Not found"));
        if (b.getStatus() != BookingStatus.CONFIRMED) throw new RuntimeException("Ticket not confirmed");

        String text = "PNR:" + b.getPnr() + "; Trip:" + b.getTrip().getId() + "; Seats:" + b.getSeats().size();
        byte[] png = qr.png(text);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(png);
    }

    // ✅ New: Download PDF ticket
    @GetMapping("/{bookingId}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable Long bookingId) throws Exception {
        byte[] pdfBytes = ticketService.generateTicketPdf(bookingId);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=ticket-" + bookingId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    // ✅ New: Email ticket as PDF attachment
    @GetMapping("/{bookingId}/email")
    public ResponseEntity<String> email(@PathVariable Long bookingId) {
        try {
            ticketService.emailTicket(bookingId);
            return ResponseEntity.ok("Ticket emailed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to send email: " + e.getMessage());
        }
    }
}
