// =========================================================
// All API models / interfaces for Akash Hotels
// =========================================================

export type Role = 'ROLE_CUSTOMER' | 'ROLE_HOTEL_ADMIN' | 'ROLE_DELIVERY_PARTNER';

export type OrderStatus =
  | 'PLACED'
  | 'CONFIRMED'
  | 'PREPARING'
  | 'READY_FOR_PICKUP'
  | 'ASSIGNED'
  | 'OUT_FOR_DELIVERY'
  | 'DELIVERED'
  | 'CANCELLED';

export type DeliveryStatus =
  | 'ASSIGNED'
  | 'ACCEPTED'
  | 'PICKED_UP'
  | 'DELIVERING'
  | 'DELIVERED'
  | 'FAILED';

export type AvailabilityStatus = 'AVAILABLE' | 'BUSY' | 'OFFLINE';

export type PaymentMethod = 'CARD' | 'UPI' | 'CASH' | 'MOCK_PAYMENT';

export type PaymentStatus = 'PENDING' | 'SUCCESS' | 'FAILED' | 'REFUNDED';

// Auth
export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  phone: string;
  role?: Role;
}

export interface AuthResponse {
  token: string;
  userId: number;
  name: string;
  email: string;
  role: Role;
}

// Hotel
export interface Hotel {
  id: number;
  name: string;
  address: string;
  city: string;
  latitude?: number;
  longitude?: number;
  active: boolean;
  createdAt?: string;
}

// Dish
export interface Dish {
  id: number;
  hotelId: number;
  hotelName: string;
  name: string;
  description: string;
  price: number;
  imageUrl: string;
  category: string;
  available: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface DishRequest {
  hotelId: number;
  name: string;
  description: string;
  price: number;
  imageUrl: string;
  category: string;
  available: boolean;
}

// Location
export interface DeliveryLocation {
  id: number;
  name: string;
  city: string;
  distanceFromHotelKm: number;
  active: boolean;
}

// Cart
export interface CartItem {
  id: number;
  dishId: number;
  dishName: string;
  dishPrice: number;
  dishImageUrl: string;
  quantity: number;
  itemTotal: number;
}

export interface Cart {
  id: number;
  customerId: number;
  items: CartItem[];
  itemCount: number;
  totalAmount: number;
}

export interface CartItemRequest {
  dishId: number;
  quantity: number;
}

// Order
export interface OrderItem {
  id: number;
  dishId: number;
  dishName: string;
  quantity: number;
  unitPrice: number;
  subtotal: number;
}

export interface Order {
  id: number;
  customerId: number;
  customerName: string;
  hotelId?: number;
  hotelName?: string;
  deliveryLocationId?: number;
  deliveryLocationName?: string;
  deliveryPartnerId?: number;
  deliveryPartnerName?: string;
  orderStatus: OrderStatus;
  subtotal: number;
  deliveryFee: number;
  totalAmount: number;
  placedAt: string;
  acceptedAt?: string;
  deliveredAt?: string;
  items: OrderItem[];
}

export interface CreateOrderRequest {
  deliveryLocationId: number;
  paymentMethod: PaymentMethod;
}

export interface OrderStatusUpdateRequest {
  status: OrderStatus;
}

export interface AssignPartnerRequest {
  deliveryPartnerId: number;
}

// Delivery Partner
export interface DeliveryPartner {
  id: number;
  userId: number;
  name: string;
  email: string;
  phone: string;
  vehicleType: string;
  vehicleNumber: string;
  availabilityStatus: AvailabilityStatus;
  active: boolean;
  joinedAt?: string;
}

export interface DeliveryPartnerRequest {
  userId: number;
  vehicleType: string;
  vehicleNumber: string;
  active?: boolean;
}

// Delivery
export interface Delivery {
  id: number;
  orderId: number;
  deliveryPartnerId: number;
  partnerName: string;
  pickupTime?: string;
  deliveryStartTime?: string;
  deliveredTime?: string;
  distanceKm: number;
  baseEarning?: number;
  bonusAmount?: number;
  totalEarning?: number;
  deliveryStatus: DeliveryStatus;
}

export interface PartnerEarnings {
  partnerId: number;
  partnerName: string;
  completedDeliveries: number;
  totalBaseEarning: number;
  totalBonusEarning: number;
  totalEarning: number;
  deliveries: Delivery[];
}

// Delivery Rules
export interface DistanceRule {
  id: number;
  minDistanceKm: number;
  maxDistanceKm: number;
  baseAmount: number;
  active: boolean;
}

export interface BonusRule {
  id: number;
  maxMinutes: number;
  bonusAmount: number;
  active: boolean;
}

export interface DistanceRuleRequest {
  minDistanceKm: number;
  maxDistanceKm: number;
  baseAmount: number;
  active: boolean;
}

export interface BonusRuleRequest {
  maxMinutes: number;
  bonusAmount: number;
  active: boolean;
}

// Payment
export interface Payment {
  id: number;
  orderId: number;
  amount: number;
  paymentMethod: PaymentMethod;
  paymentStatus: PaymentStatus;
  transactionReference?: string;
  paidAt?: string;
}

// Error response
export interface ErrorResponse {
  timestamp: string;
  status: number;
  message: string;
  path: string;
}

// Toast
export interface Toast {
  id: string;
  message: string;
  type: 'success' | 'error' | 'info';
}
