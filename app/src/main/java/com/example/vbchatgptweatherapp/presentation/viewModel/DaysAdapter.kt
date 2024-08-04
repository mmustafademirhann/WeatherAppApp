import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vbchatgptweatherapp.data.modelModelModel.WtWtWeather
import com.example.vbchatgptweatherapp.databinding.ItemDayWeatherBinding


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
