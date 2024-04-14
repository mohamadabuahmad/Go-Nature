package inviterCC;
import entities.*;
/**
 * @author anood 
 */
public class PaymentController {

    final static int PRICE_PER_CARD = 50;

    /**
     * Calculates the total price for an order considering various discounts.
     * 
     * @param order The order to calculate the price for.
     * @return The total price after applying applicable discounts.
     */
    public static double calculateOrderPrice(Order order) {
        double basePrice = PRICE_PER_CARD * order.getNumOfVisitors();
        double discount = calculateDiscount(order, basePrice);
        return basePrice - discount;
    }

    /**
     * Constructs a detailed bill for an order including all discounts applied.
     * 
     * @param order The order to generate the bill for.
     * @return A detailed bill as a String.
     */
    public static String getInvoice(Order order) {
        StringBuilder bill = new StringBuilder();
        double basePrice = PRICE_PER_CARD * order.getNumOfVisitors();
        double discount = calculateDiscount(order, basePrice);
        double finalPrice = basePrice - discount;

        bill.append(String.format("Price before discount: %d * %d = %.2f%n%n", 
                                  order.getNumOfVisitors(), PRICE_PER_CARD, basePrice));

        if (discount > 0) {
            bill.append(String.format("Total Discount Applied: -%.2f%n%n", discount));
        }

        bill.append(String.format("Final Price: %.2f%n", finalPrice));
        return bill.toString();
    }

    /**
     * Helper method to calculate the total discount based on the order type and payment status.
     * 
     * @param order The order to calculate the discount for.
     * @param basePrice The base price before discounts.
     * @return The total discount amount.
     */
    private static double calculateDiscount(Order order, double basePrice) {
        double discount = 0;

        switch (order.getOrderType()) {
            case PLANNEDTRAVELER:
                discount += basePrice * 0.15; // 15% discount
                break;
            case PLANNEDGUIDE:
                discount += PRICE_PER_CARD; // Guide's ticket is free
                discount += (basePrice - PRICE_PER_CARD) * 0.25; // 25% discount on the rest
                if (order.getPayStatus()) {
                    discount += (basePrice - PRICE_PER_CARD) * 0.12; // Additional 12% discount for paying in app
                }
                break;
            case UNPLANNEDGUIDE:
                discount += basePrice * 0.1; // 10% guide discount
                break;
            default:
                // No additional discounts apply
                break;
        }

        return discount;
    }
}
