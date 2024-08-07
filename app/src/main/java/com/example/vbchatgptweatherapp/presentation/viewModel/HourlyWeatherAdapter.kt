import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vbchatgptweatherapp.data.modelModelModel.WtWtWeather
import com.example.vbchatgptweatherapp.databinding.ItemHourlyWeatherBinding

class HourlyWeatherAdapter(
    private val hourlyWeather: List<WtWtWeather.WWW>
) : RecyclerView.Adapter<HourlyWeatherAdapter.HourlyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHourlyWeatherBinding.inflate(inflater, parent, false) // Adjust based on your item layout
        return HourlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val weather = hourlyWeather[position]
        holder.bind(weather)
    }

    override fun getItemCount(): Int = hourlyWeather.size

    class HourlyViewHolder(private val binding: ItemHourlyWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(weather: WtWtWeather.WWW) {
            binding.timeTextView.text = weather.dtTxt?.substring(11, 16) // Extract hour from dtTxt
            binding.temperatureTextView.text = "${weather.main?.temp?.let { kelvinToCelsius(it) }}°"
            // Set weather icon based on weather condition
        }

        private fun kelvinToCelsius(kelvin: Double): String {
            return (kelvin - 273.15).toInt().toString()
        }
    }



}
