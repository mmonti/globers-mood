package com.globant.labs.mood.service.mail.token.impl;

import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.User;
import com.globant.labs.mood.service.mail.token.AbstractBaseTokenGenerator;
import com.globant.labs.mood.service.mail.token.UserTokenGenerator;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
public class UserTokenGeneratorImpl extends AbstractBaseTokenGenerator implements UserTokenGenerator {

    /**
     *
     * @param secret
     */
    public UserTokenGeneratorImpl(final String secret) {
        super(secret);
    }

    /**
     *
     * @param campaign
     * @param target
     * @return
     */
    @Override
    public String getToken(final Campaign campaign, final User target) {
        final Hasher hasher = Hashing.md5().newHasher();
        hasher.putString(getSecret());
        hasher.putLong(campaign.getId());
        hasher.putString(campaign.getCreated().toString());
        hasher.putString(target.getEmail());

        return hasher.hash().toString();
    }
}
