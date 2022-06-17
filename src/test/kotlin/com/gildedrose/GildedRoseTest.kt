package com.gildedrose

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class GildedRoseTest {

    @Test
    fun foo() {
        val items = arrayOf(Item("fixme", 0, 0))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals("fixme", app.items[0].name)
    }

    @ParameterizedTest
    @ValueSource(strings = [Aged, Backstage, Sulfuras, Conjured, Dexterity, Elixir])
    fun `Quality cannot be negative`() {
        val items = arrayOf(Item(Elixir, 1, 1))
        val app = GildedRose(items)
        repeat(67) {
            app.updateQuality()
        }
        assert(app.items[0].quality >= 0)
    }

    @ParameterizedTest
    @ValueSource(strings = [Aged, Backstage, Sulfuras, Conjured, Dexterity, Elixir])
    fun `Quality cannot be greater than 50`(name: String) {
        val items = arrayOf(Item(name, 1, 49))
        val app = GildedRose(items)
        repeat(67) {
            app.updateQuality()
        }
        assert(app.items[0].quality <= 50)
    }

    @ParameterizedTest
    @ValueSource(strings = [Elixir, Dexterity])
    fun `Default item every day quality reduces by 1`(name: String) {
        val items = arrayOf(Item(name, 3, 6))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals(5, app.items[0].quality)
    }

    @ParameterizedTest
    @ValueSource(strings = [Elixir, Dexterity])
    fun `Default item every day quality reduces by 2 when sellIn is passed`(name: String) {
        val items = arrayOf(Item(name, 0, 40))
        val app = GildedRose(items)
        app.updateQuality()
        app.updateQuality()
        assertEquals(36, app.items[0].quality)
    }

    @Test
    fun `Aged should increment quality by 1 everyday`() {
        val items = arrayOf(Item(Aged, 5, 6))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals(7, app.items[0].quality)
    }

    @Test
    fun `Aged should increment quality by 2 when sellIn date is passed`() {
        val items = arrayOf(Item(Aged, 0, 6))
        val app = GildedRose(items)
        app.updateQuality()
        app.updateQuality()
        assertEquals(10, app.items[0].quality)
    }

    @Test
    fun `Backstage should increment quality by 3 when less than 5 days left`() {
        val items = arrayOf(Item(Backstage, 3, 6))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals(9, app.items[0].quality)
    }

    @Test
    fun `Backstage should increment quality by 2 when less than 10 days left`() {
        val items = arrayOf(Item(Backstage, 8, 6))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals(8, app.items[0].quality)
    }

    @Test
    fun `Backstage should increment quality by 1 everyday`() {
        val items = arrayOf(Item(Backstage, 12, 6))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals(7, app.items[0].quality)
    }

    @Test
    fun `Backstage quality should be 0 when sellIn date is passed`() {
        val items = arrayOf(Item(Backstage, 1, 26))
        val app = GildedRose(items)
        app.updateQuality()
        app.updateQuality()
        assertEquals(0, app.items[0].quality)
    }

    @Test
    fun `Sulfuras quality never changes`() {
        val items = arrayOf(Item(Sulfuras, 3, 6))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals(6, app.items[0].quality)
    }

    @Test
    fun `Sulfuras sellIn never changes`() {
        val items = arrayOf(Item(Sulfuras, 3, 6))
        val app = GildedRose(items)
        app.updateQuality()
        assertEquals(3, app.items[0].sellIn)
    }

    @Test
    fun `Item coverage`() {
        val item = Item(Conjured, 3, 6)
        assertEquals("Conjured Mana Cake, 3, 6", item.toString())
    }
}
