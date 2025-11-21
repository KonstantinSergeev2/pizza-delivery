package com.dmkcompany.pizza.service;

import com.dmkcompany.pizza.model.Cart;
import com.dmkcompany.pizza.model.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;


class CartServiceTest {

    private CartService cartService;

    @BeforeEach
    void setUp() throws Exception {
        cartService = new CartService();
        cartService.init();

    }

    // Пятый тест (1), добавление нового товара в пустую корзину.
    @Test
    void addItemWhenEmptyCartShouldAddNewItem() {
        CartItem newItem = new CartItem(1L, "Классическая", 450.0, 1, "/images/classic.avif");
    
        cartService.addItem(newItem);
        Cart cart = cartService.getCart();
        // Убеждаемся, что товар добавлен и суммы посчитаны верно
        assertEquals(1, cart.getItems().size(), "В корзине должен быть 1 товар");
        assertEquals(1, cart.getTotalItems(), "Общее количество товаров должно быть 1");
        assertEquals(450.0, cart.getTotalAmount(), 0.01, "Общая сумма должна быть 450 рублей");
    }

    // Шестой тест (2), добавление существующего товара. Должно увеличиваться количество.
    @Test
    void addItemWhenItemExistsShouldIncreaseQuantity() {
        CartItem existingItem = new CartItem(1L, "Классическая", 450.0, 1, "/images/classic.avif");
        CartItem sameItem = new CartItem(1L, "Классическая", 450.0, 2, "/images/classic.avif");
        
        cartService.addItem(existingItem);
        cartService.addItem(sameItem);
        Cart cart = cartService.getCart();
        

        assertEquals(1, cart.getItems().size(), "Должен остаться 1 элемент в корзине");
        assertEquals(3, cart.getTotalItems(), "Общее количество должно быть 3");
        assertEquals(1350.0, cart.getTotalAmount(), 0.01, "Сумма должна быть 1350 рублей");
    }

    // Седьмой тест (3), добавление товара с тем же ID но разным названием (разные размеры пиццы)
    @Test
    void addItemWhenSameIdButDifferentNameShouldAddAsNewItem() {
        CartItem mediumPizza = new CartItem(1L, "Классическая (30 см)", 450.0, 1, "/images/classic.avif");
        CartItem largePizza = new CartItem(1L, "Классическая (35 см)", 585.0, 1, "/images/classic.avif");
        
        cartService.addItem(mediumPizza);
        cartService.addItem(largePizza);
        Cart cart = cartService.getCart();
        
        assertEquals(2, cart.getItems().size(), "Должно быть 2 разных товара (разные размеры)");
        assertEquals(2, cart.getTotalItems(), "Общее количество должно быть 2");
        assertEquals(1035.0, cart.getTotalAmount(), 0.01, "Сумма должна быть 1035 рублей");
    }

    // Восьмой тест (4), удаление товара из корзины
    @Test
    void removeItemWhenItemExistsShouldRemoveFromCart() {
        CartItem item1 = new CartItem(1L, "Классическая", 450.0, 1, "/images/classic.avif");
        CartItem item2 = new CartItem(2L, "Домашняя", 520.0, 1, "/images/domash.avif");
        
        cartService.addItem(item1);
        cartService.addItem(item2);
        
        cartService.removeItem(item1);
        Cart cart = cartService.getCart();
        
        assertEquals(1, cart.getItems().size(), "Должен остаться 1 товар");
        assertEquals(1, cart.getTotalItems(), "Общее количество должно быть 1");
        assertEquals(520.0, cart.getTotalAmount(), 0.01, "Сумма должна быть 520 рублей");
        assertEquals("Домашняя", cart.getItems().get(0).getProductName(), "Оставшийся товар - Домашняя");
    }

    // Девятый тест (5), обновление количества товаров в корзине.
    @Test
    void updateQuantityWhenValidQuantityShouldUpdateItem() {
        CartItem item = new CartItem(1L, "Классическая", 450.0, 1, "/images/classic.avif");
        cartService.addItem(item);
        CartItem itemInCart = cartService.getCartItemByProductId(1L);
        
        cartService.updateQuantity(itemInCart, 3);
        Cart cart = cartService.getCart();
        
        assertEquals(3, itemInCart.getQuantity(), "Количество должно быть обновлено до 3");
        assertEquals(1350.0, cart.getTotalAmount(), 0.01, "Сумма должна быть 1350 рублей");
    }

    // Десятый тест (6), минимальное количество товара - 1.
    @Test
    void updateQuantityWhenMinimumQuantity() {
        CartItem item = new CartItem(1L, "Классическая", 450.0, 2, "/images/classic.avif");
        cartService.addItem(item);
        CartItem itemInCart = cartService.getCartItemByProductId(1L);
        
        cartService.updateQuantity(itemInCart, 1);
        
        assertEquals(1, itemInCart.getQuantity(), "Количество должно быть 1");
    }

    // Одиннадцатый тест (7), очистка всей корзины.
    @Test
    void clearCartWhenItemsExistShouldRemoveAllItems() {
        CartItem item1 = new CartItem(1L, "Классическая", 450.0, 1, "/images/classic.avif");
        CartItem item2 = new CartItem(2L, "Домашняя", 520.0, 2, "/images/domash.avif");
        
        cartService.addItem(item1);
        cartService.addItem(item2);
        
        cartService.clearCart();
        Cart cart = cartService.getCart();
        
        assertTrue(cart.getItems().isEmpty(), "Корзина должна быть пустой");
        assertEquals(0, cart.getTotalItems(), "Общее количество должно быть 0");
        assertEquals(0.0, cart.getTotalAmount(), 0.01, "Сумма должна быть 0");
    }
}