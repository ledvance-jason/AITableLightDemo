package com.ledvance.ai.light.model

import com.ledvance.ui.component.IRadioGroupItem

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 11:22
 * Describe : LightSegment
 */
enum class LightSegment(override val title: String, override val value: Int) : IRadioGroupItem<Int> {
    Segment1("1", value = 1),
    Segment2("2", value = 2),
    Segment3("3", value = 3),
    Segment4("4", value = 4),
    Segment5("5", value = 5),
    ;

    companion object {
        val allSegment = listOf(
            Segment1,
            Segment2,
            Segment3,
            Segment4,
            Segment5,
        )
    }
}