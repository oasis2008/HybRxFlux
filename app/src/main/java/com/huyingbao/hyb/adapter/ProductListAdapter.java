package com.huyingbao.hyb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huyingbao.hyb.R;
import com.huyingbao.hyb.model.Product;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    /**
     * 点击回调接口
     */
    public interface OnProductClicked {
        void onClicked(Product product);
    }

    private OnProductClicked onProductClickCallBack;

    private ArrayList<Product> mProductList;


    /**
     * 构造方法,初始化数据list
     */
    public ProductListAdapter() {
        super();
        mProductList = new ArrayList<Product>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.i_card_repository, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Product repo = mProductList.get(i);
        viewHolder.setShowData(repo);
        viewHolder.itemView.setOnClickListener(view -> {
            if (onProductClickCallBack != null) onProductClickCallBack.onClicked(repo);
        });
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    /**
     * 适配数据
     *
     * @param productList
     */
    public void setProductList(ArrayList<Product> productList) {
        this.mProductList.clear();
        this.mProductList.addAll(productList);
        notifyDataSetChanged();
    }

    /**
     * 设置点击回调
     *
     * @param onProductClickCallBack
     */
    public void setOnProductClickCallBack(OnProductClicked onProductClickCallBack) {
        this.onProductClickCallBack = onProductClickCallBack;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.description)
        public TextView descView;
        @BindView(R.id.id)
        public TextView idView;
        @BindView(R.id.name)
        TextView nameView;
        @BindView(R.id.imageView)
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setShowData(Product product) {
            nameView.setText(product.getProductName());
        }
    }
}
