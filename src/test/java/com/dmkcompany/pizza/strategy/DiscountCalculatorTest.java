package com.dmkcompany.pizza.strategy;

import com.dmkcompany.pizza.model.Cart;
import com.dmkcompany.pizza.model.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DiscountCalculatorTest {


    @Mock
    private QuantityDiscountStrategy quantityStrategy;

    @Mock  
    private AmountDiscountStrategy amountStrategy;

    private DiscountCalculator discountCalculator;
    
    private List<DiscountStrategy> discountStrategies;

    @BeforeEach
    public void setUp() {
        discountStrategies = Arrays.asList(quantityStrategy, amountStrategy);
        
        discountCalculator = new DiscountCalculator(discountStrategies);
    }

    // Первый тест, проверка комбинированных скидок.
    @Test
    public void calculateDiscountAmountWhenOrderOver1000ShouldApplyBothDiscounts() {
        Cart cart = new Cart();
        
        CartItem pizza1 = new CartItem(1L, "Классическая", 450.0, 2, "/images/classic.avif");
        CartItem cola = new CartItem(11L, "Добрый Кола", 90.0, 2, "/images/dobr.avif");
        CartItem sauce = new CartItem(5L, "Кетчуп", 50.0, 1, "/images/ket.avif");
        
        cart.setItems(Arrays.asList(pizza1, cola, sauce));

        // Обе стратегии дают скидки.
        when(quantityStrategy.calculateDiscount(cart)).thenReturn(0.05); // 5% за 5+ товаров
        when(amountStrategy.calculateDiscount(cart)).thenReturn(0.10);   // 10% за 1000+ рублей

        // Рассчитываем скидку.
        Double actualDiscount = discountCalculator.calculateDiscountAmount(cart);

        assertEquals(170.0, actualDiscount, 0.01, 
            "Скидка должна быть 15% от 1130 рублей = 170 рублей");
    }

    // Второй тест, проверка пустой корзины.
    @Test
    public void calculateDiscountAmountWhenEmptyCartShouldReturnZero() {
        Cart cart = new Cart();
        cart.setItems(Arrays.asList());

        // Никаких скидок.
        when(quantityStrategy.calculateDiscount(cart)).thenReturn(0.0);
        when(amountStrategy.calculateDiscount(cart)).thenReturn(0.0);

        Double actualDiscount = discountCalculator.calculateDiscountAmount(cart);

        // Для пустой корзины скидка должна быть 0.
        assertEquals(0.0, actualDiscount, 0.01, 
            "Для пустой корзины скидка должна быть 0");
    }

    // Третий тест, только скидка за количество товаров (<=5).
    @Test
    public void calculateDiscountAmountWhen5ItemsUnder1000ShouldApplyOnlyQuantityDiscount() {
        Cart cart = new Cart();
        
        CartItem pizza = new CartItem(4L, "4 сыра", 490.0, 1, "/images/sirnaya.avif");
        CartItem tea1 = new CartItem(9L, "Чай черный", 80.0, 2, "/images/chern-tea.avif");
        CartItem tea2 = new CartItem(10L, "Чай зеленый", 80.0, 2, "/images/green-tea.avif");
        CartItem sauce = new CartItem(7L, "Кисло-сладкий", 60.0, 1, "/images/kisl-slad.avif");
        
        cart.setItems(Arrays.asList(pizza, tea1, tea2, sauce));

        when(quantityStrategy.calculateDiscount(cart)).thenReturn(0.05);
        when(amountStrategy.calculateDiscount(cart)).thenReturn(0.0);

        Double actualDiscount = discountCalculator.calculateDiscountAmount(cart);

        assertEquals(44.0, actualDiscount, 0.01, 
            "Скидка должна быть 5% от 870 рублей = 44 рубля");
    }

    // Четвёртвый тест, проверка только скидки за сумму (1000+ рублей, но меньше 5 товаров)
    @Test
    public void calculateDiscountAmountWhenOver1000ButLessItemsShouldApplyOnlyAmountDiscount() {
        Cart cart = new Cart();
        
        CartItem pizza1 = new CartItem(2L, "Домашняя", 520.0, 2, "/images/domash.avif");
        
        cart.setItems(Arrays.asList(pizza1));


        when(quantityStrategy.calculateDiscount(cart)).thenReturn(0.0);
        when(amountStrategy.calculateDiscount(cart)).thenReturn(0.10);

        Double actualDiscount = discountCalculator.calculateDiscountAmount(cart);

        assertEquals(104.0, actualDiscount, 0.01, 
            "Скидка должна быть 10% от 1040 рублей = 104 рубля");
    }
}