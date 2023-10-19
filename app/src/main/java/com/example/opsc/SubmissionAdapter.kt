import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.opsc.R

data class Submission(val category: String, val amount: String, val comments: String)


class SubmissionsAdapter(private val submissionsList: List<Submission>) :
    RecyclerView.Adapter<SubmissionsAdapter.SubmissionViewHolder>() {

    class SubmissionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryTextView: TextView = itemView.findViewById(R.id.textViewCategory)
        private val amountTextView: TextView = itemView.findViewById(R.id.textViewAmount)
        private val commentsTextView: TextView = itemView.findViewById(R.id.textViewComments)

        fun bind(submission: Submission) {
            categoryTextView.text = submission.category
            amountTextView.text = submission.amount
            commentsTextView.text = submission.comments
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubmissionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_submission, parent, false)
        return SubmissionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SubmissionViewHolder, position: Int) {
        val submission = submissionsList[position]
        holder.bind(submission)
    }

    override fun getItemCount(): Int {
        return submissionsList.size
    }
}