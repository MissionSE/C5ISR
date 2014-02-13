package com.missionse.mapsexample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.missionse.mapsexample.model.MyItem;

import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MarkerClusterActivity extends Activity implements
        ClusterManager.OnClusterClickListener<MyItem>,
        ClusterManager.OnClusterInfoWindowClickListener<MyItem>,
        ClusterManager.OnClusterItemClickListener<MyItem>,
        ClusterManager.OnClusterItemInfoWindowClickListener<MyItem> {

    private static final String TAG = MarkerClusterActivity.class.getSimpleName();
    private GoogleMap mMap;
    private ClusterManager<MyItem> mClusterManager;

    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple items in the cluster, draw multiple photos (using MultiDrawable).
     */
    private class MyItemRenderer extends DefaultClusterRenderer<MyItem> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public MyItemRenderer() {
            super(getApplicationContext(), getMap(), mClusterManager);

            View multiItem = getLayoutInflater().inflate(R.layout.multi_item, null);
            mClusterIconGenerator.setContentView(multiItem);
            mClusterImageView = (ImageView) multiItem.findViewById(R.id.image);

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_item_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_item_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            mImageView.setImageResource(item.mPhoto);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.mName);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<MyItem> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (MyItem item : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
                Drawable drawable = getResources().getDrawable(item.mPhoto);
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_cluster);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap != null) {
            return;
        }
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        if (mMap != null) {
            startDemo();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.marker_cluster, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    private void startDemo() {
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.974500, -74.976600), 5));

        mClusterManager = new ClusterManager<MyItem>(this, mMap);
        mClusterManager.setRenderer(new MyItemRenderer());
        getMap().setOnCameraChangeListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        try {
            readItems();
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }

        mClusterManager.cluster();
    }

    private void readItems() throws JSONException {
        InputStream inputStream = getResources().openRawResource(R.raw.locations);
        List<MyItem> items = MyItemReader.read(this, inputStream);
        mClusterManager.addItems(items);
    }

    private GoogleMap getMap() {
        setUpMapIfNeeded();
        return mMap;
    }

    @Override
    public boolean onClusterClick(Cluster<MyItem> myItemCluster) {
        return false;
    }

    @Override
    public boolean onClusterItemClick(MyItem myItem) {
        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<MyItem> myItemCluster) {

    }

    @Override
    public void onClusterItemInfoWindowClick(MyItem myItem) {

    }
}
