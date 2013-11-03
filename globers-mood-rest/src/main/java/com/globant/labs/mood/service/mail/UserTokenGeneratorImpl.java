package com.globant.labs.mood.service.mail;

import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.User;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * @author mauro.monti (monti.mauro@gmail.com)
 */
@Component
public class UserTokenGeneratorImpl extends BaseTokenGeneratorImpl implements UserTokenGenerator {

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
        final StringBuilder builder = new StringBuilder(getSecret());
        builder.append(campaign.getName());
        builder.append(campaign.getCreated());
        builder.append(target.getEmail());

        return DigestUtils.md5DigestAsHex(builder.toString().getBytes());
    }
}
