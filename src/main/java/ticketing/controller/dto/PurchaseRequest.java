package ticketing.controller.dto;

public record PurchaseRequest(String eventCode, String ticketType, int quantity) { }
