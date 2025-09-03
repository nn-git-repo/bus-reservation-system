import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import API from "../api/axiosConfig";
import { Container, Card, Button } from "react-bootstrap";

export default function Ticket() {
  const { bookingId } = useParams();
  const [qrBase64, setQrBase64] = useState(null);

  useEffect(() => {
    (async () => {
      try {
        const qrResp = await API.get(`/tickets/${bookingId}/qr`, { responseType: 'blob' });
        const blob = qrResp.data;
        const reader = new FileReader();
        reader.onloadend = () => setQrBase64(reader.result.split(',')[1]);
        reader.readAsDataURL(blob);
      } catch (e) {
        console.log("QR fetch failed");
      }
    })();
  }, [bookingId]);

  const downloadPDF = async () => {
    try {
      const res = await API.get(`/tickets/${bookingId}/pdf`, { responseType: 'blob' });
      const url = window.URL.createObjectURL(new Blob([res.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `ticket-${bookingId}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (err) {
      alert("Failed to download PDF");
    }
  };

  const sendEmail = async () => {
    try {
      await API.get(`/tickets/${bookingId}/email`);
      alert("Ticket emailed (if backend configured).");
    } catch {
      alert("Failed to send email (backend may not support).");
    }
  };

  return (
    <Container className="my-5 d-flex justify-content-center">
      <Card
        className="p-4 shadow"
        style={{ maxWidth: 600, width: "100%", borderRadius: "12px", backgroundColor: "#fdfdfd" }}
      >
        <h3 className="text-center mb-4" style={{ color: "#0d6efd", fontWeight: "600" }}>
          Ticket — Booking #{bookingId}
        </h3>

        <p><strong>Booking ID:</strong> {bookingId}</p>

        <div className="text-center my-3">
          {qrBase64 ? (
            <img
              src={`data:image/png;base64,${qrBase64}`}
              alt="QR"
              style={{ width: 200, height: 200, border: "1px solid #ddd", borderRadius: "12px", padding: "5px" }}
            />
          ) : (
            <p>No QR available</p>
          )}
        </div>

        <div className="d-flex justify-content-center mt-3 flex-wrap">
          <Button
            variant="primary"
            onClick={downloadPDF}
            className="me-2 mb-2"
            style={{ borderRadius: "8px", fontWeight: "500", padding: "8px 20px" }}
          >
            Download PDF
          </Button>
          <Button
            variant="secondary"
            onClick={sendEmail}
            className="mb-2"
            style={{ borderRadius: "8px", fontWeight: "500", padding: "8px 20px" }}
          >
            Send Email
          </Button>
        </div>
      </Card>
    </Container>
  );
}
