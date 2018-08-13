package org.nearbyshops.serviceprovider.ItemsByCategorySimple;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nearbyshops.serviceprovider.ItemsByCategorySimple.EditItem.EditItem;
import org.nearbyshops.serviceprovider.ItemsByCategorySimple.EditItem.EditItemFragmentNew;
import org.nearbyshops.serviceprovider.ItemsByCategorySimple.EditItemCategory.EditItemCategory;
import org.nearbyshops.serviceprovider.ItemsByCategorySimple.EditItemCategory.EditItemCategoryFragment;
import org.nearbyshops.serviceprovider.ItemsByCategorySimple.EditItemCategory.PrefItemCategory;
import org.nearbyshops.serviceprovider.ItemsByCategorySimple.EditItemOld.UtilityItemOld;
import org.nearbyshops.serviceprovider.ItemsByCategorySimple.Utility.HeaderItemsList;
import org.nearbyshops.serviceprovider.Model.Item;
import org.nearbyshops.serviceprovider.Model.ItemCategory;
import org.nearbyshops.serviceprovider.ModelStats.ItemStats;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.Utility.UtilityGeneral;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by sumeet on 19/12/15.
 */


public class AdapterSimple extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Map<Integer,ItemCategory> selectedItemCategories = new HashMap<>();
    Map<Integer,Item> selectedItems = new HashMap<>();



    private List<Object> dataset;
    private Context context;
    private NotificationsFromAdapter notificationReceiver;

    public static final int VIEW_TYPE_ITEM_CATEGORY = 1;
    public static final int VIEW_TYPE_ITEM = 2;
    public static final int VIEW_TYPE_HEADER = 3;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 4;

    private Fragment fragment;



    public AdapterSimple(List<Object> dataset, Context context, NotificationsFromAdapter notificationReceiver,Fragment fragment) {

//        DaggerComponentBuilder.getInstance()
//                .getNetComponent().Inject(this);

        this.notificationReceiver = notificationReceiver;
        this.dataset = dataset;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType == VIEW_TYPE_ITEM_CATEGORY)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_category,parent,false);
            return new ViewHolderItemCategory(view);
        }
        else if(viewType == VIEW_TYPE_ITEM)
        {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_guide,parent,false);
            return new ViewHolderItemSimple(view);
        }
        else if(viewType == VIEW_TYPE_HEADER)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header_type_simple,parent,false);
            return new ViewHolderHeader(view);
        }
        else if(viewType == VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar,parent,false);

            return new LoadingViewHolder(view);
        }


//        else
//        {
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_guide,parent,false);
//            return new ViewHolderItemSimple(view);
//        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof ViewHolderItemCategory)
        {
            bindItemCategory((ViewHolderItemCategory) holder,position);
        }
        else if(holder instanceof ViewHolderItemSimple)
        {
            bindItem((ViewHolderItemSimple) holder,position);
        }
        else if(holder instanceof ViewHolderHeader)
        {
            if(dataset.get(position) instanceof HeaderItemsList)
            {
                HeaderItemsList header = (HeaderItemsList) dataset.get(position);

                ((ViewHolderHeader) holder).header.setText(header.getHeading());
            }

        }
        else if(holder instanceof LoadingViewHolder)
        {


            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;

            if(fragment instanceof ItemCategoriesFragmentSimple)
            {
                int fetched_count  = ((ItemCategoriesFragmentSimple) fragment).fetched_items_count;
                int items_count = ((ItemCategoriesFragmentSimple) fragment).item_count_item;

                if(fetched_count == items_count)
                {
                    viewHolder.progressBar.setVisibility(View.GONE);
                }
                else
                {
                    viewHolder.progressBar.setVisibility(View.VISIBLE);
                    viewHolder.progressBar.setIndeterminate(true);

                }
            }
        }


    }


    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);


        if(position == dataset.size())
        {
            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else if(dataset.get(position) instanceof ItemCategory)
        {
            return VIEW_TYPE_ITEM_CATEGORY;
        }
        else if (dataset.get(position) instanceof Item)
        {
            return VIEW_TYPE_ITEM;
        }
        else if(dataset.get(position) instanceof HeaderItemsList)
        {
            return VIEW_TYPE_HEADER;
        }


        return -1;
    }

    @Override
    public int getItemCount() {

        return (dataset.size() + 1);
    }




    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }



    class ViewHolderHeader extends RecyclerView.ViewHolder{


        @BindView(R.id.header)
        TextView header;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }// ViewHolder Class declaration ends



    void bindItemCategory(ViewHolderItemCategory holder,int position)
    {

        if(dataset.get(position) instanceof ItemCategory)
        {
            ItemCategory itemCategory = (ItemCategory) dataset.get(position);

            holder.categoryName.setText(String.valueOf(itemCategory.getCategoryName()));


            if(selectedItemCategories.containsKey(itemCategory.getItemCategoryID()))
            {
                //context.getResources().getColor(R.color.gplus_color_2)
                holder.itemCategoryListItem.setBackgroundColor(ContextCompat.getColor(context,R.color.gplus_color_2));
            }
            else
            {
                //context.getResources().getColor(R.color.white)
                holder.itemCategoryListItem.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
            }



            String imagePath = UtilityGeneral.getServiceURL(context) + "/api/v1/ItemCategory/Image/five_hundred_"
                    + itemCategory.getImagePath() + ".jpg";

            Drawable placeholder = VectorDrawableCompat
                    .create(context.getResources(),
                            R.drawable.ic_nature_people_white_48px, context.getTheme());

            Picasso.with(context).load(imagePath)
                    .placeholder(placeholder)
                    .into(holder.categoryImage);

        }
    }



    class ViewHolderItemCategory extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {


        @BindView(R.id.name) TextView categoryName;
        @BindView(R.id.itemCategoryListItem) ConstraintLayout itemCategoryListItem;
        @BindView(R.id.categoryImage) ImageView categoryImage;
        @BindView(R.id.cardview) CardView cardView;


        public ViewHolderItemCategory(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


        @OnClick(R.id.itemCategoryListItem)
        public void itemCategoryListItemClick()
        {
            notificationReceiver.notifyRequestSubCategory(
                    (ItemCategory) dataset.get(getLayoutPosition()));

            selectedItemCategories.clear();
            selectedItems.clear();
        }



        @OnLongClick(R.id.itemCategoryListItem)
        boolean listItemLongClick()
        {

//            showToastMessage("Long Click !");

            ItemCategory itemCategory = (ItemCategory) dataset.get(getLayoutPosition());


            if(selectedItemCategories.containsKey(
                    itemCategory.getItemCategoryID()
            ))
            {
                selectedItemCategories.remove(itemCategory.getItemCategoryID());

            }else
            {

                selectedItemCategories.put(itemCategory.getItemCategoryID(),itemCategory);


            }



            notificationReceiver.notifyItemCategorySelected();
            notifyItemChanged(getLayoutPosition());

            return true;
        }



        @OnClick(R.id.more_options)
        void optionsOverflowClick(View v)
        {
            PopupMenu popup = new PopupMenu(context, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.item_category_item_overflow, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {


            switch (item.getItemId())
            {
                case R.id.action_remove:

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Confirm Delete Item Category !")
                            .setMessage("Do you want to delete this Item Category ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if(dataset.get(getLayoutPosition()) instanceof ItemCategory)
                                    {
                                        notificationReceiver
                                                .notifyDeleteItemCat(
                                                        (ItemCategory) dataset.get(getLayoutPosition()),
                                                        getLayoutPosition()
                                                );
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    showToastMessage("Cancelled !");

                                }
                            })
                            .show();
                    break;



                case R.id.action_edit:


                    if(dataset.get(getLayoutPosition()) instanceof ItemCategory)
                    {
                        PrefItemCategory.saveItemCategory((ItemCategory) dataset.get(getLayoutPosition()),context);

                        Intent intent = new Intent(context,EditItemCategory.class);
                        intent.putExtra(EditItemCategoryFragment.EDIT_MODE_INTENT_KEY,EditItemCategoryFragment.MODE_UPDATE);
                        context.startActivity(intent);
                    }

                    break;



//                case R.id.action_detach:
//
////                    showToastMessage("Detach");
//
//                    if(dataset.get(getLayoutPosition()) instanceof ItemCategory)
//                    {
//                        notificationReceiver.detachItemCat((ItemCategory) dataset.get(getLayoutPosition()));
//                    }
//
//                break;

                case R.id.action_change_parent:

                    if(dataset.get(getLayoutPosition()) instanceof ItemCategory)
                    {
                        notificationReceiver.changeParentItemCat((ItemCategory) dataset.get(getLayoutPosition()));
                    }

                    break;

                default:

                    break;

            }



            return false;

        }
    }// ViewHolder Class declaration ends




    void bindItem(ViewHolderItemSimple holder,int position)
    {

        Item item = (Item) dataset.get(position);

        holder.categoryName.setText(item.getItemName());

        ItemStats itemStats = item.getItemStats();

        holder.priceRange.setText("Price Range :\nRs." + itemStats.getMin_price() + " - " + itemStats.getMax_price() + " per " + item.getQuantityUnit());
        holder.priceAverage.setText("Price Average :\nRs." + itemStats.getAvg_price() + " per " + item.getQuantityUnit());
        holder.shopCount.setText("Available in " + itemStats.getShopCount() + " Shops");
        holder.itemRating.setText(String.format("%.2f",itemStats.getRating_avg()));
        holder.ratingCount.setText("( " + String.valueOf(itemStats.getRatingCount()) + " Ratings )");


        if(selectedItems.containsKey(item.getItemID()))
        {
            //context.getResources().getColor(R.color.gplus_color_2)
            holder.itemCategoryListItem.setBackgroundColor(ContextCompat.getColor(context,R.color.gplus_color_2));
        }
        else
        {
            //context.getResources().getColor(R.color.white)
            holder.itemCategoryListItem.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
        }



        String imagePath = UtilityGeneral.getServiceURL(context)
                + "/api/v1/Item/Image/three_hundred_" + item.getItemImageURL() + ".jpg";


        Drawable drawable = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());

        Picasso.with(context).load(imagePath)
                .placeholder(drawable)
                .into(holder.categoryImage);

    }



    class ViewHolderItemSimple extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {


        @BindView(R.id.itemName) TextView categoryName;
//        TextView categoryDescription;

        @BindView(R.id.items_list_item) CardView itemCategoryListItem;
        @BindView(R.id.itemImage) ImageView categoryImage;
        @BindView(R.id.price_range) TextView priceRange;
        @BindView(R.id.price_average) TextView priceAverage;
        @BindView(R.id.shop_count) TextView shopCount;
        @BindView(R.id.item_rating) TextView itemRating;
        @BindView(R.id.rating_count) TextView ratingCount;



        public ViewHolderItemSimple(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }



            @OnClick(R.id.items_list_item)
            public void listItemClick()
            {
                Item item = (Item) dataset.get(getLayoutPosition());

                if(selectedItems.containsKey(
                        item.getItemID()
                ))
                {
                    selectedItems.remove(item.getItemID());
                }
                else
                {

                    selectedItems.put(item.getItemID(),item);

                }



                notificationReceiver.notifyItemSelected();
                notifyItemChanged(getLayoutPosition());
            }


        @OnClick(R.id.more_options)
        void optionsOverflowClick(View v)
        {
            PopupMenu popup = new PopupMenu(context, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.item_category_item_overflow, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.action_remove:

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Confirm Delete Item Category !")
                            .setMessage("Do you want to delete this Item Category ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if(dataset.get(getLayoutPosition())instanceof Item)
                                    {
                                        notificationReceiver
                                                .notifyDeleteItem(
                                                        (Item) dataset.get(getLayoutPosition()),
                                                        getLayoutPosition()
                                                );
                                    }


                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    showToastMessage("Cancelled !");
                                }
                            })
                            .show();


                    break;

                case R.id.action_edit:

                    if (dataset.get(getLayoutPosition()) instanceof Item) {

                        UtilityItemOld.saveItem((Item) dataset.get(getLayoutPosition()), context);

                        Intent intentEdit = new Intent(context, EditItem.class);
                        intentEdit.putExtra(EditItemFragmentNew.EDIT_MODE_INTENT_KEY, EditItemFragmentNew.MODE_UPDATE);
                        context.startActivity(intentEdit);
                    }


                    break;


//                case R.id.action_detach:
//
//                    if (dataset.get(getLayoutPosition()) instanceof Item) {
//                        notificationReceiver.detachItem((Item) dataset.get(getLayoutPosition()));
//                    }
//                    break;


                case R.id.action_change_parent:

                    if (dataset.get(getLayoutPosition()) instanceof Item)
                    {
                        notificationReceiver.changeParentItem((Item) dataset.get(getLayoutPosition()));
                    }
                    break;

                default:
                    break;

            }

            return false;
        }



    }// ViewHolder Class declaration ends


    private void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }







    interface NotificationsFromAdapter
    {
        // method for notifying the list object to request sub category
        void notifyRequestSubCategory(ItemCategory itemCategory);
        void notifyItemCategorySelected();
        void notifyItemSelected();

        void detachItemCat(ItemCategory itemCategory);
        void notifyDeleteItemCat(ItemCategory itemCategory, int position);
        void changeParentItemCat(ItemCategory itemCategory);

        void detachItem(Item item);
        void notifyDeleteItem(Item item, int position);
        void changeParentItem(Item item);
    }



//    private void showToastMessage(String message)
//    {
//        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
//    }

}