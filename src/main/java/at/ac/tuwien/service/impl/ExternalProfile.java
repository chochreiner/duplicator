package at.ac.tuwien.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.mapper.parameter.PageParameters.NamedPair;
import org.apache.wicket.spring.injection.annot.SpringBean;

import at.ac.tuwien.BasePage;
import at.ac.tuwien.ErrorPage;
import at.ac.tuwien.domain.KeyValueEntry;
import at.ac.tuwien.service.DBService;

public class ExternalProfile extends BasePage {

	private static final long serialVersionUID = 7734710518718389158L;

	private static final String UPLOAD_FOLDER = "appdata/images/";

	@SpringBean(name = "DBService")
	private DBService dbService;

	public ExternalProfile(PageParameters parameters) {


		List<KeyValueEntry> values = new ArrayList<KeyValueEntry>();

		Set<String> keys = parameters.getNamedKeys();

		check(keys, "birthday");
		check(keys, "prename");
		check(keys, "surname");
		check(keys, "email");
		check(keys, "password");

		String avatar = null;

		for (NamedPair param : parameters.getAllNamed()) {

			if (!param.getKey().equals("avatar")) {
				values.add(new KeyValueEntry(param.getKey(), param.getValue()));

				if (param.getKey().equals("birthday")) {
					String[] birthday = param.getValue().split("\\.");
					values.add(new KeyValueEntry("birthday_date", birthday[0]));
					values.add(new KeyValueEntry("birthday_month", birthday[1]));
					values.add(new KeyValueEntry("birthday_year", birthday[2]));
					try {
						Date birthdayDate = new SimpleDateFormat("dd.MM.yyyy").parse(param.getValue());
						values.add(new KeyValueEntry("birthday_date_without_null", new SimpleDateFormat("d").format(birthdayDate)));
						values.add(new KeyValueEntry("birthday_month_without_null", new SimpleDateFormat("M").format(birthdayDate)));
						values.add(new KeyValueEntry("birthday_month_alpha", new SimpleDateFormat("MMM").format(birthdayDate)));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			} else {
				avatar = param.getValue();
			}


		}

		String uuid = dbService.addProfile(values);

		if (avatar != null) {
			try{
				File inputFile = new File(UPLOAD_FOLDER + uuid + ".jpg");
				URL copyurl = new URL(avatar);
				InputStream outputFile = copyurl.openStream();
				FileOutputStream out = new FileOutputStream(inputFile);
				int c;
				while ((c = outputFile.read()) != -1) {
					out.write(c);
				}
				outputFile.close();
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}

	private void check(Set<String> keys, String key) {
		if (!keys.contains(key)) {
			bailout(key);
		}
	}

	private void bailout(String key) {
		PageParameters parameter = new PageParameters();

		parameter.add("error", "There is no value for the key " + key);
		throw new RestartResponseException(ErrorPage.class, parameter);
	}



}
