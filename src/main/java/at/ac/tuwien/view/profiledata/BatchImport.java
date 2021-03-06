package at.ac.tuwien.view.profiledata;

import java.io.File;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import at.ac.tuwien.BasePage;
import at.ac.tuwien.components.FileUploadForm;
import at.ac.tuwien.service.DBService;

public class BatchImport extends BasePage {

    private static final long serialVersionUID = 7734710518718389158L;

    @SpringBean(name = "DBService")
    private DBService dbService;

    private static final String UPLOAD_FOLDER = "appdata/uploads/";

    public BatchImport() {
        body.add(new AttributeModifier("id", true, new Model<String>("profiledata")));

        if (!new File(UPLOAD_FOLDER).exists()) {
            new File(UPLOAD_FOLDER).mkdirs();
        }

        final Form<Void> uploadForm = new FileUploadForm("uploadForm", UPLOAD_FOLDER) {
            private static final long serialVersionUID = -4499523543968816298L;

            @Override
            public String additionalAction() {
                return dbService.addProfile(newFile);
            }
        };

        body.add(new UploadProgressBar("progress", uploadForm));
        body.add(uploadForm);
        body.add(new FeedbackPanel("feedback"));

    }

}