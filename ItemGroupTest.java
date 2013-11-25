package com.auchan.po.item.itemgroup;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.auchan.dsl.token.item.itemgroup.ColorElementValueToken;
import com.auchan.dsl.token.item.itemgroup.SizeElementValueToken;
import com.auchan.po.item.Item;
import com.auchan.po.item.itemgroup.Color;
import com.auchan.po.item.itemgroup.Element;
import com.auchan.po.item.itemgroup.ElementValue;
import com.auchan.po.item.itemgroup.ItemGroup;
import com.auchan.po.item.itemgroup.Size;
import com.auchan.service.GroupService;

import static com.auchan.dsl.item.itemgroup.ColorBuilder.color;
import static com.auchan.dsl.item.itemgroup.ColorElementValueBuilder.colorValue;
import static com.auchan.dsl.item.itemgroup.ExtendElementBuilder.extendElement;
import static com.auchan.dsl.item.itemgroup.ExtendElementValueBuilder.extendElementValue;
import static com.auchan.dsl.item.itemgroup.ItemGroupBuilder.itemGroup;
import static com.auchan.dsl.item.itemgroup.SizeBuilder.size;
import static com.auchan.dsl.item.itemgroup.SizeElementValueBuilder.sizeValue;


// Using DSL in the Item Group 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-main.xml"		
        })
public class ItemGroupTest extends AbstractJUnit4SpringContextTests {

	@Resource
	private GroupService groupService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	 @Test
	public void itemGroupTest() {

		ItemGroup group = new ItemGroup();
		Size size = new Size();

		Set<ElementValue> elementValues = new HashSet<ElementValue>();
		ElementValue ev = new ElementValue();
		ev.setElementValue("XL");
		elementValues.add(ev);

		ev.setElement(size); // relationships

		size.setElementValues(elementValues); // relationships
		size.setGroup(group); // relationships
		size.setSize("服装尺寸");

		Set<Element> elements = new HashSet<Element>();
		elements.add(size);
		group.setElements(elements); // relationships
		group.setGroupName("服装群组");

		groupService.save(group);
	}

	@Test
	public void itemGroupWithDSL() {

		ItemGroup itemgroup = new ItemGroup();
		itemgroup.setGroupName("服装群组");

		Size size = new Size();
		size.setSize("服装尺寸");
		
		Color color = new Color();
		color.setColor("服装颜色");

		SizeElementValueToken elementValue = new SizeElementValueToken();
		elementValue.setElementValue("XL");

		SizeElementValueToken elementValue1 = new SizeElementValueToken();
		elementValue1.setElementValue("XXL");

		ColorElementValueToken elementValue2 = new ColorElementValueToken();
		elementValue2.setElementValue("white");
         
		itemgroup.include(size.of(elementValue).of(elementValue1),
				color.of(elementValue2));
		
		groupService.save(itemgroup);
	}

	@Test
	public void itemGroupWithDSL_simplified() {
		
		ItemGroup itemgroup = itemGroup().name("服装群组")
		           .include(  size()
				               .name("服装尺寸")
				               .of(sizeValue().of("XL"))
				               .of(sizeValue().of("XXL"))
				               .of(colorValue().of("white")) 
				               ,
				            color()
				               .name("服装颜色")
				               .of(colorValue().of("white")) 
				         );

		
		groupService.save(itemgroup);
	}

	
	@Test
	public void itemGroupWithDSL_simplified_extendElement() {
		
		ItemGroup itemgroup = itemGroup().name("服装群组")
		           .include(  size()
				               .name("服装尺寸")
				               .of(sizeValue().of("XL"))
				               .of(sizeValue().of("XXL"))
				               .of(colorValue().of("white")) 
				               
				               ,
				            color()
				               .name("服装颜色")
				               .of(colorValue().of("white")) 
				               ,
				               
				             extendElement()
				               .name("扩展元素")
				               .of(extendElementValue().of("扩展元素值"))
				         );

		
		groupService.save(itemgroup);
	}
	
	@Test
	public void itemGroupContainItemsTest() {

		// itemgroup
		ItemGroup itemgroup = new ItemGroup();
		itemgroup.setGroupName("男装群组");

		Set<ItemGroup> itemgroups = new HashSet<ItemGroup>();
		itemgroups.add(itemgroup);

		// // element and elementValue
		// Size size = new Size();
		// size.setSize("男装尺寸");
		//
		// Color color = new Color();
		// color.setColor("男装颜色");
		//
		// ElementValue elementValue = new ElementValue();
		// elementValue.setElementValue("XXL");
		//
		// ElementValue elementValue1 = new ElementValue();
		// elementValue1.setElementValue("XXXL");
		//
		// ElementValue elementValue2 = new ElementValue();
		// elementValue2.setElementValue("black");

		// item
		Item item = new Item();
		item.setProductName("男装item");

		Set<Item> items = new HashSet<Item>();
		items.add(item);

		// relationship
		// itemgroup.include( size.include(elementValue).include(elementValue1),
		//                    color.include(elementValue2) );

		itemgroup.setItems(items);
		item.setItemGroups(itemgroups);

		groupService.save(itemgroup);
	}

	@Test
	public void itemGroupContainItems_ForDSLTest() {

		// itemgroup
		ItemGroup itemgroup = new ItemGroup();
		itemgroup.setGroupName("女装群组");

		Set<ItemGroup> itemgroups = new HashSet<ItemGroup>();
		itemgroups.add(itemgroup);

		// item
		Item item = new Item();
		item.setProductName("女装item");

		// relationship
		itemgroup.contain(item);

		groupService.save(itemgroup);
	}
	

	
	@Test
	public void itemGroup_Items_Test_ForDSLTest() {

		// itemgroup
		ItemGroup itemgroup = new ItemGroup();
		itemgroup.setGroupName("童装群组");

		 // element and elementValue
		 Size size = new Size();
		 size.setSize("童装尺寸");
		
		 Color color = new Color();
		 color.setColor("童装颜色");
		
		 SizeElementValueToken elementValue = new SizeElementValueToken();
		 elementValue.setElementValue("S");
		
		 SizeElementValueToken elementValue1 = new SizeElementValueToken();
		 elementValue1.setElementValue("M");
		
		 ColorElementValueToken elementValue2 = new ColorElementValueToken();
		 elementValue2.setElementValue("blue");

		// item
		Item item = new Item();
		item.setProductName("童装item");

		// relationship
		itemgroup.include( size.of(elementValue).of(elementValue1),
		                    color.of(elementValue2) );
		itemgroup.contain(item);

		groupService.save(itemgroup);
	}
}