package com.globant.labs.mood.support

import com.globant.labs.mood.model.persistent.ElementMetadata
import com.globant.labs.mood.model.persistent.ElementType
import com.globant.labs.mood.model.statistics.CampaignStatistics
import org.apache.commons.math3.stat.Frequency

/**
 * Created by mmonti on 8/22/14.
 */
class StatisticsSupport {

    /**
     *
     * @param campaignStatistics
     */
    def static calculateFrequencies(final CampaignStatistics campaignStatistics) {
        def campaign = campaignStatistics.getCampaign()
        def metadata = campaign.getTemplate().getMetadata()

        def choiceSet = metadata.getElements().findAll {
            element ->
                ElementType.CHOICE.equals(element.getElementType())

        } as Set<ElementMetadata>

        def frequencies = [:]
        choiceSet.each {
            element ->

                def frequency = new Frequency();
                def key = element.getKey()
                def type = Class.forName(element.getValueType())

                campaign.collect(key, type).each { value ->
                    frequency.addValue(value)
                }

                def values = [:]
                element.getValues().each { value ->
                    def frequencyType = value.asType(Class.forName(element.getValueType()))
                    def keys = [
                            count : frequency.getCount(frequencyType),
                            pct : frequency.getPct(frequencyType),
                            cumPct : frequency.getCumPct(frequencyType),
                            cumFreq : frequency.getCumFreq(frequencyType),
                            sumFreq : frequency.getSumFreq()
                    ]
                    values.put(value, keys)
                }
                frequencies.put(element.getKey(), values)
        }
        campaignStatistics.addFrequencies(frequencies);
    }

}
