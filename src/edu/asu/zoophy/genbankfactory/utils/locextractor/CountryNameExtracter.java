package edu.asu.zoophy.genbankfactory.utils.locextractor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;

public class CountryNameExtracter {
	
	public String countries = "(Andorra|United Arab Emirates|Afghanistan|Antigua and Barbuda|Anguilla|Albania|Armenia|Angola|Antarctica|"+
						"Argentina|American Samoa|Austria|Australia|Aruba|Aland Islands|Azerbaijan|Bosnia and Herzegovina|Barbados|Bangladesh|"
						+ "Belgium|Burkina Faso|Bulgaria|Bahrain|Burundi|Benin|Saint Barthelemy|Bermuda|Brunei|"
						+ "Bolivia|Bonaire, Saint Eustatius and Saba |Brazil|Bahamas|Bhutan|Bouvet Island|Botswana|Belarus|Belize|Canada|"
						+ "Cocos Islands|Democratic Republic of the Congo|Central African Republic|Republic of the Congo|Switzerland|"
						+ "Ivory Coast|Cook Islands|Chile|Cameroon|China|Colombia|Costa Rica|Cuba|Cape Verde|Curacao|Christmas Island|"
						+ "Cyprus|Czechia|Germany|Djibouti|Denmark|Dominica|Dominican Republic|Algeria|Ecuador|Estonia|Egypt|Western Sahara|"
						+ "Eritrea|Spain|Ethiopia|Finland|Fiji|Falkland Islands|Micronesia|Faroe Islands|France|Gabon|United Kingdom|Grenada|"
						+ "Georgia|French Guiana|Guernsey|Ghana|Gibraltar|Greenland|Gambia|Guinea|Guadeloupe|Equatorial Guinea|Greece|"
						+ "South Georgia and the South Sandwich Islands|Guatemala|Guam|Guinea-Bissau|Guyana|Hong Kong|"
						+ "Heard Island and McDonald Islands|Honduras|Croatia|Haiti|Hungary|Indonesia|Ireland|Israel|Isle of Man|India|"
						+ "British Indian Ocean Territory|Iraq|Iran|Iceland|Italy|Jersey|Jamaica|Jordan|Japan|Kenya|Kyrgyzstan|Cambodia|"
						+ "Kiribati|Comoros|Saint Kitts and Nevis|North Korea|South Korea|Kosovo|Kuwait|Cayman Islands|Kazakhstan|Laos|"
						+ "Lebanon|Saint Lucia|Liechtenstein|Sri Lanka|Liberia|Lesotho|Lithuania|Luxembourg|Latvia|Libya|Morocco|Monaco|"
						+ "Moldova|Montenegro|Saint Martin|Madagascar|Marshall Islands|Macedonia|Mali|Myanmar|Mongolia|Macao|"
						+ "Northern Mariana Islands|Martinique|Mauritania|Montserrat|Malta|Mauritius|Maldives|Malawi|Mexico|Malaysia|"
						+ "Mozambique|Namibia|New Caledonia|Niger|Norfolk Island|Nigeria|Nicaragua|Netherlands|Norway|Nepal|Nauru|Niue|"
						+ "New Zealand|Oman|Panama|Peru|French Polynesia|Papua New Guinea|Philippines|Pakistan|Poland|"
						+ "Saint Pierre and Miquelon|Pitcairn|Puerto Rico|Palestinian Territory|Portugal|Palau|Paraguay|Qatar|Reunion|"
						+ "Romania|Serbia|Russia|Rwanda|Saudi Arabia|Solomon Islands|Seychelles|Sudan|South Sudan|Sweden|Singapore|"
						+ "Saint Helena|Slovenia|Svalbard and Jan Mayen|Slovakia|Sierra Leone|San Marino|Senegal|Somalia|Suriname|"
						+ "Sao Tome and Principe|El Salvador|Sint Maarten|Syria|Swaziland|Turks and Caicos Islands|Chad|"
						+ "French Southern Territories|Togo|Thailand|Tajikistan|Tokelau|Timor Leste|Turkmenistan|Tunisia|Tonga|Turkey|"
						+ "Trinidad and Tobago|Tuvalu|Taiwan|Tanzania|Ukraine|Uganda|United States Minor Outlying Islands|United States|"
						+ "Uruguay|Uzbekistan|Vatican|Saint Vincent and the Grenadines|Venezuela|British Virgin Islands|U.S. Virgin Islands|"
						+ "Vietnam|Vanuatu|Wallis and Futuna|Samoa|Yemen|Mayotte|South Africa|Zambia|Zimbabwe|Serbia and Montenegro|Netherlands Antilles)";
	public Pattern countryPattern;
	
	public  CountryNameExtracter() {
		countryPattern = Pattern.compile(countries, Pattern.CASE_INSENSITIVE);
		
	/*	try {
			
			String fileLocation = "/home/prashant/zoophy/geonames/countryInfo.txt";
			
			BufferedReader bufReader = new BufferedReader(new FileReader(fileLocation));
			StringBuilder builder = new StringBuilder();
			
			while (true) {
				String line = bufReader.readLine();
				if (line == null ) {
					break;
				}
				
				String[] columns = line.split("\t");
				
				builder.append(builder.length() == 0 ? "(":"|" );
				builder.append(columns[4]);
				
			}
			
			countryPattern = Pattern.compile(builder.append(")").toString(), Pattern.CASE_INSENSITIVE);
			System.out.println( builder.toString());
			bufReader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
}

