package com.missionse.mseuiextensionsexample.navigationdrawer;

import android.widget.TextView;

import com.missionse.mseuiextensions.R;
import com.missionse.uiextensions.navigationdrawer.DrawerActivity;
import com.missionse.uiextensions.navigationdrawer.DrawerItem;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfiguration;
import com.missionse.uiextensions.navigationdrawer.configuration.DrawerConfigurationContainer;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerComplexItem;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerDivider;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerHeader;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerPaddedDivider;
import com.missionse.uiextensions.navigationdrawer.entry.DrawerSimpleItem;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerActivity extends DrawerActivity {

	private static final int BASIC_ITEM = 101;
	private static final int COMPLEX_ITEM = 102;
	private static final int HEADER = 103;
	private static final int NOCHANGE_ITEM = 104;
	private static final int ICON_ITEM = 105;
	private static final int CUSTOM_SELECTION_ITEM = 106;

	private static final int DIVIDER = 95;
	private static final int PADDED_DIVIDER = 96;

	@Override
	protected DrawerConfigurationContainer getDrawerConfigurations() {
		DrawerConfigurationContainer configurationContainer = new DrawerConfigurationContainer(
			R.layout.activity_navigation_drawer, R.id.drawer_layout);

		DrawerConfiguration configuration = new DrawerConfiguration(this, DrawerConfigurationContainer.DrawerType.LEFT,
			R.id.left_drawer);
		configurationContainer.addConfiguration(configuration);

		return configurationContainer;
	}

	@Override
	protected void onDrawerConfigurationComplete() {
		List<DrawerItem> menu = new ArrayList<DrawerItem>();

		menu.add(DrawerSimpleItem.create(BASIC_ITEM, getResources().getString(R.string.basic_item), 0, true));
		menu.add(DrawerDivider.create(DIVIDER));
		menu.add(DrawerComplexItem.create(COMPLEX_ITEM, getResources().getString(R.string.complex_item),
			getResources().getString(R.string.complex_item_subtitle), 0, true));
		menu.add(DrawerHeader.create(HEADER, getResources().getString(R.string.item_header)));
		menu.add(DrawerComplexItem.create(NOCHANGE_ITEM, getResources().getString(R.string.nochange_item),
			getResources().getString(R.string.nochange_item_subtitle), 0, false));
		menu.add(DrawerPaddedDivider.create(PADDED_DIVIDER));
		menu.add(DrawerSimpleItem.create(ICON_ITEM, getResources().getString(R.string.icon_item),
			R.drawable.ic_launcher, true));
		DrawerSimpleItem customSelectionItem = DrawerSimpleItem.create(CUSTOM_SELECTION_ITEM,
			getResources().getString(R.string.custom_selection_item), 0, true);
		customSelectionItem.setBackgroundDrawable(R.drawable.list_selector, this);
		customSelectionItem.setTextColorStateList(R.drawable.text_selector, this);
		menu.add(customSelectionItem);

		for (DrawerItem item : menu) {
			getLeftDrawerAdapter().add(item);
		}

		selectItem(0, getLeftDrawerList());
	}

	@Override
	protected void onNavigationItemSelected(int id) {
		TextView contentView = (TextView) findViewById(R.id.content_frame);
		String currentText = (String) contentView.getText();
		contentView.setText(currentText + "\nMenu item ID: " + id + " selected.");
	}
}
