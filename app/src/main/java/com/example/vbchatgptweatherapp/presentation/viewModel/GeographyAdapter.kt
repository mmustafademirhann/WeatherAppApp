import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vbchatgptweatherapp.data.modelModelModel.trdTry.GeographiaCityModels
import com.example.vbchatgptweatherapp.databinding.AddCityBinding
import com.example.vbchatgptweatherapp.databinding.GeographyViewholderBinding
import com.example.vbchatgptweatherapp.presentation.MainActivity

class GeographyAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<GeographyAdapter.ViewHolder>() {

    private lateinit var binding: AddCityBinding
    interface OnItemClickListener{
        fun onItemClick(lat: Double?,lon:Double?,name:String?,isFromSearch:Boolean)
        }
    inner class ViewHolder(val binding: AddCityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(city: GeographiaCityModels.GeographiaCityModelsItem) {
            binding.addCity.text = city.name
            itemView.setOnClickListener {
                listener.onItemClick(city.lat,city.lon,city.name,true)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = AddCityBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = differ.currentList[position]
        Log.d("GeographyAdapter", "Binding city: ${city.name}")
        holder.bind(city)
    }

    private val differCallback =
        object : DiffUtil.ItemCallback<GeographiaCityModels.GeographiaCityModelsItem>() {
            override fun areItemsTheSame(
                oldItem: GeographiaCityModels.GeographiaCityModelsItem,
                newItem: GeographiaCityModels.GeographiaCityModelsItem
            ): Boolean {
                return oldItem.lat == newItem.lat && oldItem.lon == newItem.lon
            }

            override fun areContentsTheSame(
                oldItem: GeographiaCityModels.GeographiaCityModelsItem,
                newItem: GeographiaCityModels.GeographiaCityModelsItem
            ): Boolean {
                return oldItem == newItem
            }
        }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int = differ.currentList.size

    fun updateGeoData(newGeoData: List<GeographiaCityModels.GeographiaCityModelsItem>) {
        differ.submitList(newGeoData)
    }
}
