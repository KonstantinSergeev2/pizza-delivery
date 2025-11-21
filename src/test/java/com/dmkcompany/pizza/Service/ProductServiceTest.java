package com.dmkcompany.pizza.service;

import com.dmkcompany.pizza.model.Product;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


class ProductServiceTest {

    private final ProductService productService = new ProductServiceImpl();

    // Двенадцатый тест (1), тестируем поиск товаров по категории "пицца".
    @Test
    void getProductsByCategoryWhenPizzaCategory_ShouldReturnOnlyPizzas() {
        List<Product> pizzas = productService.getProductsByCategory(1L);
        
        assertEquals(4, pizzas.size(), "Должно быть 4 пиццы в каталоге");
        assertTrue(pizzas.stream().allMatch(p -> p.getCategoryId().equals(1L)), 
            "Все товары должны принадлежать категории пицц");
        
        List<String> pizzaNames = pizzas.stream().map(Product::getName).toList();
        assertTrue(pizzaNames.contains("Классическая"), "Должна быть Классическая пицца");
        assertTrue(pizzaNames.contains("Домашняя"), "Должна быть Домашняя пицца");
    }

    // Тринадцатый тест (2), тестируем поиск товара по ID.
    @Test
    void getProductByIdWhenExistingIdShouldReturnCorrectProduct() {
        Product product = productService.getProductById(1L);
        
        assertNotNull(product, "Должен вернуться товар (не null)");
        assertEquals("Классическая", product.getName(), "Должна быть Классическая пицца");
        assertEquals(450.0, product.getBasePrice(), 0.01, "Цена должна быть 450 рублей");
        assertEquals(1L, product.getCategoryId(), "Категория должна быть 'Пиццы'");
    }

    // Четырнадцатый тест (3), поиск несуществующего товара (с неверным ID).
    @Test
    void getProductByIdWhenNonExistingIdShouldReturnNull() {
        Product product = productService.getProductById(999L);
        
        assertNull(product, "Для несуществующего ID должен возвращаться null");
    }
}