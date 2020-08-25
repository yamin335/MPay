package com.qpay.customer.ui.home

import androidx.lifecycle.ViewModel
import com.qpay.customer.R
import com.qpay.customer.models.Book
import com.qpay.customer.models.SubBook
import com.qpay.customer.prefs.PreferencesHelper
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val preferencesHelper: PreferencesHelper) :
    ViewModel() {
    val doctorList: List<Book>
        get() = listOf(
            Book(
                "0",
                "Top Rated",
                listOf(
                    SubBook(
                        "1",
                        "Angelica Hale",
                        "Dhaka",
                        "",
                        R.drawable.book_1
                    ),
                    SubBook(
                        "1",
                        "Angelica Hale",
                        "Dhaka",
                        "",
                        R.drawable.book_2
                    ),
                    SubBook(
                        "1",
                        "Angelica Hale",
                        "Dhaka",
                        "",
                        R.drawable.book_3
                    )
                )
            ),
            Book(
                "1",
                "Favourites",
                listOf(
                    SubBook(
                        "1",
                        "Angelica Hale",
                        "Dhaka",
                        "",
                        R.drawable.book_4
                    ),
                    SubBook(
                        "1",
                        "Angelica Hale",
                        "Dhaka",
                        "",
                        R.drawable.book_13
                    ),
                    SubBook(
                        "1",
                        "Angelica Hale",
                        "Dhaka",
                        "",
                        R.drawable.book_6
                    )
                )
            ),
            Book(
                "2",
                "Top Rated",
                listOf(
                    SubBook(
                        "1",
                        "Angelica Hale",
                        "Dhaka",
                        "",
                        R.drawable.book_7
                    ),
                    SubBook(
                        "1",
                        "Angelica Hale",
                        "Dhaka",
                        "",
                        R.drawable.book_8
                    ),
                    SubBook(
                        "1",
                        "Angelica Hale",
                        "Dhaka",
                        "",
                        R.drawable.book_9
                    )
                )
            )
        )
}