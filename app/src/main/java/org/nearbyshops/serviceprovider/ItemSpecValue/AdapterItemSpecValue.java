package org.nearbyshops.serviceprovider.ItemSpecValue;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import com.squareup.picasso.Picasso;

import org.nearbyshops.serviceprovider.ModelItemSpecification.ItemSpecificationValue;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.Preferences.PrefGeneral;

import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 13/6/16.
 */
class AdapterItemSpecValue extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ItemSpecificationValue> dataset = null;
    private NotificationsFromAdapter notifyFragment;
    private Context context;
    private Fragment fragment;


    private final static int VIEW_TYPE_PROGRESS_BAR = 2;
    private final static int VIEW_TYPE_VALUE = 1;


    AdapterItemSpecValue(List<ItemSpecificationValue> dataset, NotificationsFromAdapter notifyFragment,
                         Context context, Fragment fragment) {
        this.dataset = dataset;
        this.notifyFragment = notifyFragment;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType==VIEW_TYPE_VALUE)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_item_spec_value,parent,false);

            return new ViewHolderItemSpecValue(view);

        }
        else if(viewType ==VIEW_TYPE_PROGRESS_BAR)
        {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar,parent,false);

            return new AdapterItemSpecValue.LoadingViewHolder(view);
        }

        return null;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderGiven, int position) {

        if(holderGiven instanceof ViewHolderItemSpecValue)
        {
            ItemSpecificationValue itemSpecificationValue = dataset.get(position);

            ViewHolderItemSpecValue holder = (ViewHolderItemSpecValue) holderGiven;

            holder.titleItemSpec.setText(itemSpecificationValue.getTitle());
            holder.description.setText(itemSpecificationValue.getDescription());
            holder.itemCount.setText("Item Count : " + String.valueOf(itemSpecificationValue.getRt_item_count()));

            Drawable drawable = ContextCompat.getDrawable(context,R.drawable.ic_nature_people_white_48px);
            String imagePath = PrefGeneral.getServiceURL(context) + "/api/v1/ItemSpecificationValue/Image/" + "three_hundred_"+ itemSpecificationValue.getImageFilename() + ".jpg";

            Picasso.with(context)
                    .load(imagePath)
                    .placeholder(drawable)
                    .into(holder.imageItemSpec);
        }
        else if(holderGiven instanceof AdapterItemSpecValue.LoadingViewHolder)
        {

            AdapterItemSpecValue.LoadingViewHolder viewHolder = (AdapterItemSpecValue.LoadingViewHolder) holderGiven;

            int itemCount = 0;

            if(fragment instanceof ItemSpecValueFragment)
            {
                itemCount = ((ItemSpecValueFragment) fragment).item_count;
            }


            if(position == 0 || position == itemCount)
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



    @Override
    public int getItemCount() {
        return (dataset.size()+1);
    }


    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);

        if(position==dataset.size())
        {
            return VIEW_TYPE_PROGRESS_BAR;
        }
        else
        {
            return VIEW_TYPE_VALUE;
        }
    }




    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }









    class ViewHolderItemSpecValue extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {


        @BindView(R.id.title_item_spec) TextView titleItemSpec;
        @BindView(R.id.image_item_spec) ImageView imageItemSpec;
        @BindView(R.id.description) TextView description;
        @BindView(R.id.item_count) TextView itemCount;

        public ViewHolderItemSpecValue(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


        @OnClick(R.id.more_options)
        void optionsOverflowClick(View v)
        {
            PopupMenu popup = new PopupMenu(context, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.item_spec_name_item_overflow, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }



        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId())
            {

                case R.id.action_remove:

//                    showToastMessage("Remove");
                    notifyFragment.removeItemSpecName(dataset.get(getLayoutPosition()),getLayoutPosition());

                    break;

                case R.id.action_edit:

//                    showToastMessage("Edit");
                    notifyFragment.editItemSpecName(dataset.get(getLayoutPosition()),getLayoutPosition());

                    break;


                default:
                    break;

            }

            return false;
        }



    }





    interface NotificationsFromAdapter{

        void addItemImage();
        void editItemSpecName(ItemSpecificationValue itemSpecValue, int position);
        void removeItemSpecName(ItemSpecificationValue itemSpecValue, int position);
    }

}
