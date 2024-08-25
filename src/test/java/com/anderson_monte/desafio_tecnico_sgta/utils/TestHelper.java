package com.anderson_monte.desafio_tecnico_sgta.utils;

import com.anderson_monte.desafio_tecnico_sgta.dtos.ItemInput;
import com.anderson_monte.desafio_tecnico_sgta.dtos.ItemOutput;
import com.anderson_monte.desafio_tecnico_sgta.dtos.ListInput;
import com.anderson_monte.desafio_tecnico_sgta.dtos.ListOutput;
import com.anderson_monte.desafio_tecnico_sgta.models.Item;
import com.anderson_monte.desafio_tecnico_sgta.models.List;
import com.anderson_monte.desafio_tecnico_sgta.models.State;

import java.util.ArrayList;

public class TestHelper {
    public static List createList() {
        return new List("List name");
    }

    public static List createPersistedList() {
        return new List(1L, "List name", new ArrayList<>());
    }

    public static ListInput createListInput() {
        return new ListInput("List name");
    }

    public static ListOutput createListOutput() {
        return new ListOutput(1L, "List name", new ArrayList<>());
    }

    public static Item createItem(List list) {
      return   new Item(null,"Item name", list);
    }

    public static Item createPersistedItem() {
        var list = new List(1L, "List name", new ArrayList<>());
        return new Item(1L, "Item name", list);
    }
    public static ItemInput createItemInput(List list) {
        return new ItemInput(1L, "Item name", list.getName());
    }

    public static List createListWithItems() {
        var list = new List(1L, "List name", new ArrayList<>());
        list.getItems().add(new Item(null, "Item name", list));
        return list;
    }

    public static ListOutput createListOutputWithItems() {
        var item = new ItemOutput(1L, "Item name", State.PENDING.name(), false );
        var list = new ListOutput(1L, "List name", new ArrayList<>());
        list.items().add(item);
        return list;
    }

    public static Item itemHighlight(List list) {
        var item = new Item(null, "Item name", list);
        item.highlight();
        return item;
    }

    public static ItemOutput itemOutputHighlight() {
        return new ItemOutput(1L, "ITEM NAME", State.PENDING.name(), true);
    }
}
