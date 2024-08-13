import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.vbchatgptweatherapp.data.modelModelModel.GeoModel
import com.example.vbchatgptweatherapp.data.modelModelModel.secndTry.GeoCityModel
import com.example.vbchatgptweatherapp.data.modelModelModel.trdTry.GeographiaCityModels
import com.example.vbchatgptweatherapp.databinding.GeographyViewholderBinding // Make sure this import is correct
import com.example.vbchatgptweatherapp.presentation.MainActivity

class GeographyAdapter(private var geoData: GeographiaCityModels) :
    RecyclerView.Adapter<GeographyAdapter.GeographyViewHolder>() {

    class GeographyViewHolder(val binding: GeographyViewholderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeographyViewHolder {
        val binding = GeographyViewholderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GeographyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GeographyViewHolder, position: Int) {
        val geoItem = geoData[position]
        holder.binding.cityTextView.text = geoItem.name ?: ""

        holder.itemView.setOnClickListener {
            val cityName = geoItem.name ?: ""
            val lat = geoItem.lat ?: 0.0
            val lon = geoItem.lon ?: 0.0

            val intent = Intent(holder.itemView.context, MainActivity::class.java)
            intent.putExtra("cityName", cityName)
            intent.putExtra("latitude", lat)
            intent.putExtra("longitude", lon)
            holder.itemView.context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int = geoData.size

    fun updateGeoData(newGeoData: GeographiaCityModels) {
        geoData = newGeoData
        notifyDataSetChanged()
    }
}