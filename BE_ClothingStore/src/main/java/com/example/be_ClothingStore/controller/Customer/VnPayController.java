package com.example.be_ClothingStore.controller.Customer;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.be_ClothingStore.domain.Orders;
import com.example.be_ClothingStore.domain.Payments;
import com.example.be_ClothingStore.domain.RestResponse.RestResponse;
import com.example.be_ClothingStore.service.OrderService;
import com.example.be_ClothingStore.service.PaymentService;
import com.example.be_ClothingStore.service.VnPayService;
import com.example.be_ClothingStore.service.error.IdInvalidException;
import com.example.be_ClothingStore.util.SecurityUtil;
// sàda
@org.springframework.stereotype.Controller
@RequestMapping("/customer")
public class VnPayController {
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final SecurityUtil securityUtil;
    private final VnPayService vnPayService;
    public VnPayController(PaymentService paymentService,VnPayService vnPayService, OrderService orderService, SecurityUtil securityUtil){
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.securityUtil = securityUtil;
        this.vnPayService = vnPayService;
    }  

    @PostMapping("/payments")
    public ResponseEntity<?> submidOrder(@RequestParam("totalPrice") int totalPrice,
                            @RequestParam("orderId") String orderId,
                            HttpServletRequest request) throws IdInvalidException{

        Orders order = this.orderService.findOrderById(orderId);
        if (order == null){
            throw new IdInvalidException("Không tìm thấy order với " + orderId);
        } 
        order.setStatus("paying");
        this.orderService.addAOrder(order);
        order.setStatus("paid");
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(totalPrice, orderId, baseUrl);
        if (vnpayUrl != null){
            return ResponseEntity.ok(new RestResponse<>(HttpStatus.OK.value(), "Call API success", "Call API success", vnpayUrl));
        } else {
            return ResponseEntity.badRequest().body(new RestResponse<>(HttpStatus.BAD_REQUEST.value(), "Call API fail", "Call API fail", "vnpayUrl: " + vnpayUrl));
        }
        
    }

    @GetMapping("/vnpay-payment")
    public ResponseEntity<?> GetMappingURL(HttpServletRequest request) throws IdInvalidException{
        String token = this.securityUtil.getTokenFromCookie(request);
        if (token == null) {
            throw new IdInvalidException("Không tìm thấy token trong cookie!");
        }
        String ownerId = this.securityUtil.getUserIdFromToken(token);

        String paymentStatus = vnPayService.orderReturn(request);
        String orderId = request.getParameter("vnp_OrderInfo");
        String bankName = request.getParameter("vnp_BankCode");
        
        // Đảm bảo chỉ có 1 payment cho 1 order
        Payments newPayment = null;
        if (this.paymentService.findPaymentByOrderId(orderId) != null){
            newPayment = this.paymentService.findPaymentByOrderId(orderId);
        } else{
            newPayment = new Payments();
        }
        newPayment.setUserID(ownerId);
        newPayment.setOrderID(orderId);
        newPayment.setBankName(bankName);
        newPayment.setStatus(paymentStatus);
        switch (paymentStatus) {
            case "00":
                newPayment.setErrorMessage("Giao dịch thành công");
                break;
            case "01":
                newPayment.setErrorMessage("Giao dịch chưa hoàn tất");
                break;
            case "02":
                newPayment.setErrorMessage("Giao dịch bị từ chối");
                break;
            case "03":
                newPayment.setErrorMessage("Giao dịch bị từ chối");
                break;
            case "04":
                newPayment.setErrorMessage("Giao dịch bị từ chối");
                break;
            case "05":
                newPayment.setErrorMessage("Giao dịch bị từ chối");
                break;
            case "06":
                newPayment.setErrorMessage("Giao dịch bị từ chối");
                break;
            case "07":
                newPayment.setErrorMessage("Giao dịch bị từ chối");
                break;
            case "08":
                newPayment.setErrorMessage("Giao dịch bị từ chối");
                break;
            case "09":
                newPayment.setErrorMessage("Giao dịch bị từ chối");
                break;
            default:
                newPayment.setErrorMessage("Giao dịch thất bại với lỗi" +paymentStatus);
        }
        
        
        Orders order = this.orderService.findOrderById(orderId);
        if (order == null){
            throw new IdInvalidException("Không tìm thấy order với " + orderId);
        }
        if ("00".equals(paymentStatus)) {
            order.setStatus("paid");
        } else {
            order.setStatus("paying");
        }
        Payments payment = this.paymentService.createAPayment(newPayment);
        order.setPaymentID(payment.getId());
        this.orderService.addAOrder(order);
        return ResponseEntity.ok().body(payment);
    }
}