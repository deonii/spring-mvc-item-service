package hello.itemservice.web.basic;


import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

//    @PostMapping("/add")
//    public String addItemV1(
//            @RequestParam String itemName,
//            @RequestParam int price,
//            @RequestParam Integer quantity,
//            Model model
//    ) {
//        Item item = new Item();
//        item.setItemName(itemName);
//        item.setPrice(price);
//        item.setQuantity(quantity);
//        itemRepository.save(item);
//
//        model.addAttribute("item", item);
//        return "basic/item";
//    }

//    @PostMapping("/add")
//    public String addItemV2(@ModelAttribute("item") Item item) {
//        itemRepository.save(item);
//        return "basic/item";
//    }

//    @PostMapping("/add")
//    public String addItemV3(@ModelAttribute Item item, Model model) {
//        itemRepository.save(item);
//        return "basic/item";
//    }

//    @PostMapping("/add")
//    public String addItemV4(Item item) {
//        itemRepository.save(item);
//        return "basic/item";
//    }

//    @PostMapping("/add")
//    public String addItemV5(Item item) {
//        itemRepository.save(item);
//        return "redirect:/basic/items/" + item.getId();
//    }

//    @PostMapping("/add")
//    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
//        Item savedItem = itemRepository.save(item);
//        redirectAttributes.addAttribute("itemId", savedItem.getId());
//        redirectAttributes.addAttribute("status", true);
//
//        return "redirect:/basic/items/{itemId}";
//    }

//    @PostMapping("/add")
//    public String addItemErrorV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
//
//        if(!StringUtils.hasText(item.getItemName())){
//            bindingResult.addError(new FieldError("item", "itemName", "상품명은 필수입니다."));
//        }
//
//        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
//            bindingResult.addError(new FieldError("item", "itemPrice", "가격 범위에서 벗어납니다."));
//        }
//
//        if(item.getQuantity() == null || item.getQuantity() >= 9999) {
//            bindingResult.addError(new FieldError("item", "itemQuantity", "수량 범위에서 벗어납니다."));
//        }
//
//        if(item.getPrice() != null && item.getQuantity() != null) {
//            int resultPrice = item.getPrice() * item.getQuantity();
//            if(resultPrice < 10000) {
//                bindingResult.addError(new ObjectError("item", "가격 수량 통합이 10000이상이여야 합니다."));
//            }
//        }
//
//        if(bindingResult.hasErrors()) {
//            log.info("errors = {}", bindingResult);
//            return "basic/addForm";
//        }
//
//        Item savedItem = itemRepository.save(item);
//        redirectAttributes.addAttribute("itemId", savedItem.getId());
//        redirectAttributes.addAttribute("status", true);
//
//        return "redirect:/basic/items/{itemId}";
//    }

    @PostMapping("/add")
    public String addItemErrorV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        itemValidator.validate(item, bindingResult);

        if(bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "basic/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }


    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}
