package com.globant.labs.mood.service.mail.template.handlebars.helpers;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.globant.labs.mood.model.mail.MailMessage;
import com.globant.labs.mood.model.persistent.Attachment;
import com.globant.labs.mood.model.persistent.Campaign;
import com.globant.labs.mood.model.persistent.Template;
import com.google.appengine.repackaged.com.google.common.base.Optional;
import com.google.appengine.repackaged.com.google.common.base.Predicate;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.util.Set;

import static com.google.appengine.repackaged.com.google.common.collect.Iterables.tryFind;

/**
 * Created by mmonti on 8/14/14.
 */
public class ContentIDHelper implements Helper<MailMessage> {

    private static final String EMPTY = "";
    private static final String CONTENT_ID_PREFIX = "cid:";

    @Override
    public CharSequence apply(MailMessage context, Options options) throws IOException {
        final String filename = options.param(0);
        Preconditions.checkNotNull(filename, "filename is null");

        final Campaign campaign = context.getCampaign();
        final Template template = campaign.getTemplate();
        final Set<Attachment> attachments = template.getAttachments();
        final Optional<Attachment> attachment = tryFind(attachments, new Predicate<Attachment>() {
            @Override
            public boolean apply(final Attachment attachment) {
                return attachment.getFilename().equals(filename);
            }
        });
        if (attachment.isPresent()) {
            return CONTENT_ID_PREFIX + attachment.get().getFilenameID();
        }
        return EMPTY;
    }
}
