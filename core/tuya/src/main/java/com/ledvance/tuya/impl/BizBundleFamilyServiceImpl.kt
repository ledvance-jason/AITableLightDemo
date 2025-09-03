package com.ledvance.tuya.impl

import com.thingclips.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/3 14:40
 * Describe : BizBundleFamilyServiceImpl
 */
class BizBundleFamilyServiceImpl : AbsBizBundleFamilyService() {
    private var mHomeId: Long = 0
    override fun getCurrentHomeId(): Long {
        return mHomeId
    }

    override fun shiftCurrentFamily(familyId: Long, curName: String?) {
        super.shiftCurrentFamily(familyId, curName)
        mHomeId = familyId
    }
}