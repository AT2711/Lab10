package vn.edu.ueh.thanhdnh.firebase_example;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ArticleViewHolder extends RecyclerView.ViewHolder {

  private TextView txtTitle;
  private TextView txtContent;
  private TextView txtViews;
  private ImageView imgCover;

  private ArticleViewAdapter adapter;

  public ArticleViewHolder(
          @NonNull View itemView,
          ArticleViewAdapter adapter) {

    super(itemView);

    txtTitle = itemView.findViewById(R.id.txt_name);
    txtContent = itemView.findViewById(R.id.txt_phone);
    txtViews = itemView.findViewById(R.id.txt_views);
    imgCover = itemView.findViewById(R.id.img_cover);

    this.adapter = adapter;
  }

  public TextView getTxtTitle() {
    return txtTitle;
  }

  public TextView getTxtContent() {
    return txtContent;
  }

  public TextView getTxtViews() {
    return txtViews;
  }

  public ImageView getImgCover() {
    return imgCover;
  }
}