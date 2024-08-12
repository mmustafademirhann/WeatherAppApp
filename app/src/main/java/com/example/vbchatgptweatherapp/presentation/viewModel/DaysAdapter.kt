import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vbchatgptweatherapp.R
import com.example.vbchatgptweatherapp.data.daysHandling.WeatherItem
import com.example.vbchatgptweatherapp.data.modelModelModel.WtWtWeather
import com.example.vbchatgptweatherapp.databinding.ItemDayWeatherBinding

class DaysAdapter(
    private var weatherData: WtWtWeather,
    private var groupedWeather: Map<String, List<WtWtWeather.WWW>>,
    private val onDayClickListener: ((String) -> Unit)? // Pass the date as a string
) : RecyclerView.Adapter<DaysAdapter.DaysViewHolder>() {private val dayList = groupedWeather.keys.toList()
    var expandedDay: String? = null // Track the currently expanded day

    val weatherItems = mutableListOf<WeatherItem>()

    init {
        updateWeatherItems()
        // Extract unique dates and create day items with their hourly data
        for ((date, hourlyWeather) in groupedWeather) {
            weatherItems.add(WeatherItem(hourlyWeather, date))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDayWeatherBinding.inflate(inflater, parent, false)
        return DaysViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        val weatherItem = weatherItems[position]
        holder.bind(weatherItem, expandedDay == weatherItem.date) // Pass expansion state

        holder.itemView.setOnClickListener {
            onDayClickListener?.invoke(weatherItem.date) // Pass the clicked day's date
        }
    }

    override fun getItemCount(): Int = weatherItems.size

    fun updateExpandedDay(newExpandedDay: String?) {
        expandedDay = newExpandedDay
        notifyDataSetChanged() // Refresh the adapter to reflect the change
    }
    class DaysViewHolder(val binding: ItemDayWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherItem: WeatherItem, isExpanded: Boolean) {
            binding.dateTextView.text = weatherItem.date
            binding.temperatureChildTextView.text = "${weatherItem.hourlyWeather.firstOrNull()?.main?.temp?.let { kelvinToCelsius(it) }}°"
            val weatherCondition = weatherItem.hourlyWeather.firstOrNull()?.weather?.firstOrNull()?.main
            //val weatherCondition = weatherItem.hourlyWeather.firstOrNull()?.weather?.firstOrNull()?.main

            // Update the image based on the weather condition
            when (weatherCondition) {
                "Clouds" -> binding.imageChildView.setImageResource(R.drawable.cloud_mnc)
                "Rain" -> binding.imageChildView.setImageResource(R.drawable.rainy_img_mnc)
                "Snow" -> binding.imageChildView.setImageResource(R.drawable.snowy)else -> binding.imageChildView.setImageResource(R.drawable.sunny_cloudy_mnc)
            }
            binding.hourlyDataContainer.removeAllViews() // Clear previous views
            if (isExpanded) {
                for (hourlyData in weatherItem.hourlyWeather) {
                    val hourView = LayoutInflater.from(binding.root.context).inflate(
                        R.layout.item_hourly_weather, // Use your item_hourly_weather layout
                        binding.hourlyDataContainer,
                        false
                    )
                    val timeTextView = hourView.findViewById<TextView>(R.id.timeTextView)
                    val temperatureTextView = hourView.findViewById<TextView>(R.id.temperatureTextView)
                    // Set weather icon in weatherIconImageView (if needed)
                    val weatherIconImageView = hourView.findViewById<ImageView>(R.id.weatherIconImageView) // Define weatherIconImageView here
                    timeTextView.text = hourlyData.dtTxt?.substring(11, 16)
                    temperatureTextView.text = "${hourlyData.main?.temp?.let { kelvinToCelsius(it) }}°"
                    val weatherConditio = hourlyData.weather?.firstOrNull()?.main
                    when (weatherConditio) {
                        "Clouds" -> weatherIconImageView.setImageResource(R.drawable.cloud_mnc)
                        "Rain" -> weatherIconImageView.setImageResource(R.drawable.rainy_img_mnc)
                        "Snow" -> weatherIconImageView.setImageResource(R.drawable.snowy)
                        else -> weatherIconImageView.setImageResource(R.drawable.sunny_cloudy_mnc)
                    }




                    binding.hourlyDataContainer.addView(hourView)
                }
                binding.hourlyDataContainer.visibility = View.VISIBLE
            } else {
                binding.hourlyDataContainer.visibility = View.GONE
            }
        }

        private fun kelvinToCelsius(kelvin: Double): String {
            return (kelvin - 273.15).toInt().toString()
        }

    }




    fun updateWeatherData(newWeatherData: WtWtWeather, newGroupedWeather: Map<String, List<WtWtWeather.WWW>>) {
        weatherData = newWeatherData
        groupedWeather = newGroupedWeather
        updateWeatherItems() // Update the weatherItems list
        notifyDataSetChanged()
    }


    private fun updateWeatherItems() {
        weatherItems.clear()
        for ((date, hourlyWeather) in groupedWeather) {
            weatherItems.add(WeatherItem(hourlyWeather, date))
        }
    }
}

