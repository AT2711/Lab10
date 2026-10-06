package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

class ArticleViewAdapter extends RecyclerView.Adapter<ArticleViewHolder> {

  private LayoutInflater mInflater;
  private List<Article> articles;

  public ArticleViewAdapter(
          Context context,
          List<Article> articles) {

    mInflater = LayoutInflater.from(context);
    this.articles = articles;
  }

  public void update(List<Article> articles) {
    this.articles = articles;
  }

  @NonNull
  @Override
  public ArticleViewHolder onCreateViewHolder(
          @NonNull ViewGroup parent,
          int viewType) {

    View view = mInflater.inflate(
            R.layout.contact_list,
            parent,
            false
    );

    return new ArticleViewHolder(
            view,
            this
    );
  }

  @Override
  public void onBindViewHolder(
          @NonNull ArticleViewHolder holder,
          int position) {

    Article article = articles.get(position);

    holder.getTxtTitle().setText(
            article.getTitle()
    );

    holder.getTxtContent().setText(
            article.getContent()
    );

    holder.getTxtViews().setText(
            "Views: " + article.getViews()
    );

    Glide.with(holder.itemView.getContext())
            .load(article.getImg_cover())
            .placeholder(
                    android.R.drawable.ic_menu_gallery
            )
            .error(
                    android.R.drawable.ic_delete
            )
            .into(holder.getImgCover());

    holder.itemView.setOnClickListener(v -> {

      Context context =
              holder.itemView.getContext();

      Intent intent = new Intent(
              context,
              ArticleDetailActivity.class
      );

      intent.putExtra(
              "documentId",
              article.getDocumentId()
      );

      context.startActivity(intent);
    });
  }

  @Override
  public int getItemCount() {

    if (articles == null) {
      return 0;
    }

    return articles.size();
  }
}