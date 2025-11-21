package com.dmkcompany.pizza.service;

import com.dmkcompany.pizza.model.Product;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class PizzaSizeServiceTest {

    private final PizzaSizeService pizzaSizeService = new PizzaSizeService();

    // Пятнадцатый тест (1), тестируем неизвестный размер.
    @Test
    void createSizeVariant_WhenUnknownSize_ShouldReturnBaseProduct() {
        Product basePizza = new Product(2L, "Домашняя", "Пицца как у мамы - сытная и ароматная", 1L, 520.0, "/images/domash.avif");
        
        Product result = pizzaSizeService.createSizeVariant(basePizza, "unknown");
        
        assertEquals("Домашняя", result.getName(), "Название должно остаться 'Домашняя'");
        assertEquals(520.0, result.getBasePrice(), 0.01, "Цена должна остаться 520 рублей");
        assertEquals("Пицца как у мамы - сытная и ароматная", result.getDescription(), "Описание должно сохраниться");
    }

    // Шестнадцатый тест (2), тестируем разные размеры.
    @Test
    void createSizeVariantWhenFourCheesePizzaShouldCalculateCorrectPrices() {
        Product basePizza = new Product(4L, "4 сыра", "Нежное сочетание четырех видов изысканных сыров", 1L, 490.0, "/images/sirnaya.avif");
        
        Product small = pizzaSizeService.createSizeVariant(basePizza, "small");
        Product large = pizzaSizeService.createSizeVariant(basePizza, "large");
        
        assertEquals(343.0, small.getBasePrice(), 0.01, 
            "Маленькая '4 сыра' должна стоить: 490 * 0.7 = 343 рубля");
        assertEquals(637.0, large.getBasePrice(), 0.01,
            "Большая '4 сыра' должна стоить: 490 * 1.3 = 637 рублей");
    }
}