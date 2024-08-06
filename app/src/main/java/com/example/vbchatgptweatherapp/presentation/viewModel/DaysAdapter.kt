import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vbchatgptweatherapp.data.daysHandling.WeatherItem
import com.example.vbchatgptweatherapp.data.modelModelModel.WtWtWeather
import com.example.vbchatgptweatherapp.databinding.ItemDayWeatherBinding


class DaysAdapter(
    private val weatherData: WtWtWeather,
    private val groupedWeather: Map<String, List<WtWtWeather.WWW>>,
    private val onDayClickListener: ((List<WtWtWeather.WWW>) -> Unit)?
) : RecyclerView.Adapter<DaysAdapter.DaysViewHolder>() {
    private val dayList = groupedWeather.keys.toList()

    private val weatherItems = mutableListOf<WeatherItem>()

    init {
        // Extracting unique dates and create day items with their hourly data
        val groupedWeather = weatherData.list!!.groupBy { it.dtTxt?.substringBefore(" ") }
        for ((date, hourlyWeather) in groupedWeather) {
            weatherItems.add(WeatherItem(hourlyWeather, date.toString(), false)) // Initialize isExpanded to false
        }
    }

    // ... (Only DaysViewHolder is needed now)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding
        = ItemDayWeatherBinding.inflate(inflater, parent, false)
        return DaysViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position:
                                  Int) {
        val weatherItem = weatherItems[position]
        holder.bind(weatherItem, onDayClickListener)

        holder.itemView.setOnClickListener {
            weatherItem.isExpanded = !weatherItem.isExpanded
            notifyItemChanged(position)
            onDayClickListener?.invoke(weatherItem.hourlyWeather)
        }
    }

    override fun getItemCount(): Int = weatherItems.size

    class DaysViewHolder(val binding: ItemDayWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherItem: WeatherItem, onDayClickListener: ((List<WtWtWeather.WWW>) -> Unit)?) {
            binding.dateTextVEw.text = weatherItem.date
            binding.temperatureChildTextView.text = "${weatherItem.hourlyWeather.firstOrNull()?.main?.temp?.let { kelvinToCelsius(it) }}°"

            binding.hourlyDataContainer.removeAllViews()
            if (weatherItem.isExpanded) {
                // Populate hourly data if expanded
                for (hourlyData in weatherItem.hourlyWeather) {
                    val hourTextView = TextView(binding.root.context)
                    hourTextView.text = "${hourlyData.dtTxt?.substringAfter(" ")} - ${hourlyData.main?.temp?.let { kelvinToCelsius(it) }}°"
                    binding.hourlyDataContainer.addView(hourTextView)
                }
            } else {
                // Hide hourly data if collapsed
                binding.hourlyDataContainer.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                // Handle click event here if needed
            }
        }


        private fun kelvinToCelsius(kelvin: Double): String {
            return (kelvin - 273.15).toInt().toString()
        }
    }
}