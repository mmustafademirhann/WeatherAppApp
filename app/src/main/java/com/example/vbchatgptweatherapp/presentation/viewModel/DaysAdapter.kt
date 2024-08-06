import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vbchatgptweatherapp.data.daysHandling.WeatherItem
import com.example.vbchatgptweatherapp.data.modelModelModel.WtWtWeather
import com.example.vbchatgptweatherapp.databinding.ItemDayWeatherBinding

/*
class DaysAdapter(
    private val groupedWeather: Map<String, List<WtWtWeather.WWW>>,private val onDayClickListener: ((List<WtWtWeather.WWW>) -> Unit)?
) : RecyclerView.Adapter<DaysAdapter.DaysViewHolder>() {

    private val dayList = groupedWeather.keys.toList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDayWeatherBinding.inflate(inflater, parent, false) // Adjust based on your item layout
        return DaysViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        val day = dayList[position]
        val weatherForDay = groupedWeather[day] ?: emptyList()

        weatherForDay?.let {
            holder.bind(day, it, onDayClickListener) // Pass day, weather data, and the click listener
        }

        holder.itemView.setOnClickListener {
            val isVisible = holder.binding.recyclerCard.visibility == View.VISIBLE
            holder.binding.recyclerCard.visibility = if (isVisible) View.GONE else View.VISIBLE



            if (!isVisible) {
                val childAdapter = HourlyWeatherAdapter(weatherForDay)
                holder.binding.childRecyclerView.adapter = childAdapter
                holder.binding.childRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
            }
        }
    }

    override fun getItemCount(): Int = dayList.size

    class DaysViewHolder(val binding: ItemDayWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(day: String, weatherForDay: List<WtWtWeather.WWW>, onDayClickListener: ((List<WtWtWeather.WWW>) -> Unit)?) {
            binding.dateTextVEw.text = day
            binding.temperatureChildTextView.text = "${weatherForDay.firstOrNull()?.main?.temp?.let { kelvinToCelsius(it) }}°"
            binding.root.setOnClickListener {
                // Pass the hourly data for this day to the listener
                onDayClickListener?.invoke(weatherForDay)
            }
        }

        private fun kelvinToCelsius(kelvin: Double): String {
            return (kelvin - 273.15).toInt().toString()
        }
    }
}
*/


class DaysAdapter(
    private val weatherData: WtWtWeather,
    private val groupedWeather: Map<String, List<WtWtWeather.WWW>>,
    private val onDayClickListener: ((List<WtWtWeather.WWW>) -> Unit)?
) : RecyclerView.Adapter<DaysAdapter.DaysViewHolder>() {
    private val dayList=groupedWeather.keys.toList()

    private val weatherItems = mutableListOf<WeatherItem>()

    init {
        // Extract unique dates and create day items with their hourly data
        val groupedWeather = weatherData.list!!.groupBy { it.dtTxt?.substringBefore(" ") }
        for ((date, hourlyWeather) in groupedWeather) {
            weatherItems.add(WeatherItem(hourlyWeather,date.toString()))
        }
    }

    // ... (Only DaysViewHolder is needed now)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDayWeatherBinding.inflate(inflater, parent, false)
        return DaysViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        val day = dayList[position]
        val weatherForDay = groupedWeather[day] ?: emptyList()
        holder.bind(day, weatherForDay, onDayClickListener)

        holder.itemView.setOnClickListener {
            // Handle expansion/collapse or pass data to onDayClickListener
            onDayClickListener?.invoke(weatherForDay)
        }
    }
    override fun getItemCount(): Int =dayList.size



    class DaysViewHolder(val binding: ItemDayWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(day: String, hourlyWeather: List<WtWtWeather.WWW>, onDayClickListener: ((List<WtWtWeather.WWW>) -> Unit)?) {
            binding.dateTextVEw.text = day
            binding.temperatureChildTextView.text = "${hourlyWeather.firstOrNull()?.main?.temp?.let { kelvinToCelsius(it) }}°"

            // Add logic to display hourly data within the day item's layout
            // You might use a LinearLayout or other container to hold the hourly views
            binding.hourlyDataContainer.removeAllViews() // Clear previous hourly data
            for (hourlyData in hourlyWeather) {
                val hourTextView = TextView(binding.root.context)
                hourTextView.text = "${hourlyData.dtTxt?.substringAfter(" ")} - ${hourlyData.main?.temp?.let { kelvinToCelsius(it) }}°"
                binding.hourlyDataContainer.addView(hourTextView)
            }

            binding.root.setOnClickListener {
                onDayClickListener?.invoke(hourlyWeather)
            }
        }

        private fun kelvinToCelsius(kelvin: Double): String {
            return (kelvin - 273.15).toInt().toString()
        }
    }
}