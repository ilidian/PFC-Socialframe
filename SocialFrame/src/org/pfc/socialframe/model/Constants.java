package org.pfc.socialframe.model;

public class Constants {
	//Clave app de facebook
	public static final String APP_ID = "227586584004016";
	//Constantes de velocidad y distancia en los gestos de las fotos
	public static final int SWIPE_MIN_VELOCITY = 50;
	public static final int SWIPE_MIN_DISTANCE = 50;
	//Codigo para reconocer voz
	public static final int REQUEST_CODE = 1234;
	//C�digos QR
	public static final String[] EventsQR = {"calendario","fecha","blanco"};
	public static final String[] FeedQR = {"muro","pared","gris"};
	public static final String[] FriendsQR = {"agenda","contactos","verde"};
	public static final String[] InfoQR = {"carnet","tarjeta","azul"};
	public static final String[] MessagesQR = {"carta","mensaje","amarillo"};
	public static final String[] PhotosQR = {"foto","album","negro"};
	//Matriz con paises ISO
	public static final String[][] Countries = {{"AD","AE","AF","AG","AI","AL","AM","AN","AO","AQ","AR","AS","AT","AU","AW","AX","AZ",
		"BA","BB","BD","BE","BF","BG","BH","BI","BJ","BL","BM","BN","BO","BR","BS","BT","BV","BW","BY","BZ","CA","CC","CF","CG","CH",
		"CI","CK","CL","CM","CN","CO","CR","CU","CV","CX","CY","CZ","DE","DJ","DK","DM","DO","DZ","EC","EE","EG","EH","ER","ES","ET",
		"FI","FJ","FK","FM","FO","FR","GA","GB","GD","GE","GF","GG","GH","GI","GL","GM","GN","GP","GQ","GR","GS","GT","GU","GW","GY",
		"HK","HM","HN","HR","HT","HU","ID","IE","IL","IM","IN","IO","IQ","IR","IS","IT","JE","JM","JO","JP","KE","KG","KH","KI","KM",
		"KN","KP","KR","KW","KY","KZ","LA","LB","LC","LI","LK","LR","LS","LT","LU","LV","LY","MA","MC","MD","ME","MG","MH","MK","ML",
		"MM","MN","MO","MQ","MR","MS","MT","MU","MV","MW","MX","MY","MZ","NA","NC","NE","NF","NG","NI","NL","NO","NP","NR","NU","NZ",
		"OM","PA","PE","PF","PG","PH","PK","PL","PM","PN","PR","PS","PT","PW","PY","QA","RE","RO","RS","RU","RW","SA","SB","SC","SD",
		"SE","SG","SH","SI","SJ","SK","SL","SM","SN","SO","SR","ST","SV","SY","SZ","TC","TD","TF","TG","TH","TH","TJ","TK","TL","TM",
		"TN","TO","TR","TT","TV","TW","UA","UG","US","UY","UZ","VA","VC","VE","VG","VI","VN","VU","WF","WS","YE","YT","ZA"},{"Andorra",
		"Emiratos �rabes Unidos","Afganist�n","Antigua y Barbuda","Anguila","Albania","Armenia","Antillas Neerlandesas","Angola","Ant�rtida",
		"Argentina","Samoa Americana","Austria","Australia","Aruba","I. �land","Azerbaiy�n","Bosnia y Herzegovina","Barbados","Bangladesh",
		"B�lgica","Burkina Faso","Bulgaria","Bahr�in","Burundi","Benin","San Bartolom�","Bermudas","Brun�i","Bolivia","Brasil","Bahamas",
		"Bhut�n","I. Bouvet","Botsuana","Belar�s","Belice","Canad�","I. Cocos","Rep. Centro-Africana","Congo","Suiza","Costa de Marfil",
		"I. Cook","Chile","Camer�n","China","Colombia","Costa Rica","Cuba","Cabo Verde","I. Christmas","Chipre","Rep. Checa","Alemania",
		"Yibuti","Dinamarca","Dom�nica","Rep. Dominicana","Argel","Ecuador","Estonia","Egipto","Sahara","Eritrea","Espa�a","Etiop�a","Finlandia",
		"Fiji","I. Malvinas","Micronesia","I. Feroe","Francia","Gab�n","Reino Unido","Granada","Georgia","Guayana Francesa","Guernsey","Ghana",
		"Gibraltar","Groenlandia","Gambia","Guinea","Guadalupe","Guinea Ecuatorial","Grecia","Georgia del Sur","Guatemala","Guam","Guinea-Bissau",
		"Guayana","Hong Kong","I. Heard y McDonald","Honduras","Croacia","Hait�","Hungr�a","Indonesia","Irlanda","Israel","I. de Man","India",
		"T.B. Oc�ano �ndico","Irak","Ir�n","Islandia","Italia","Jersey","Jamaica","Jordania","Jap�n","Kenia","Kirguist�n","Camboya","Kiribati",
		"Comoros","San Crist�bal","Corea del Norte","Corea del Sur","Kuwait","I. Caim�n","Kazajst�n","Laos","L�bano","Santa Luc�a","Liechtenstein",
		"Sri Lanka","Liberia","Lesotho","Lituania","Luxemburgo","Letonia","Libia","Marruecos","M�naco","Moldavia","Montenegro","Madagascar","I. Marshall",
		"Macedonia","Mali","Myanmar","Mongolia","Macao","Martinica","Mauritania","Montserrat","Malta","Mauricio","Maldivas","Malawi","M�xico","Malasia",
		"Mozambique","Namibia","Nueva Caledonia","N�ger","I. Norkfolk","Nigeria","Nicaragua","Pa�ses Bajos","Noruega","Nepal","Nauru","Niue","Nueva Zelanda",
		"Om�n","Panam�","Per�","Polinesia Francesa","Pap�a Nueva Guinea","Filipinas","Pakist�n","Polonia","San Pedro y Miquel�n","I. Pitcairn","Puerto Rico",
		"Palestina","Portugal","I. Palaos","Paraguay","Qatar","Reuni�n","Ruman�a","Serbia","Rusia","Ruanda","Arabia Saudita","I. Solom�n","Seychelles","Sud�n",
		"Suecia","Singapur","Santa Elena","Eslovenia","I. Svalbard","Eslovaquia","Sierra Leona","San Marino","Senegal","Somalia","Surinam","Santo Tom� y Pr�ncipe",
		"El Salvador","Siria","Suazilandia","I. Turcas y Caicos","Chad","T. Australes Franceses","Togo","Tailandia","Tanzania","Tayikist�n","Tokelau","Timor-Leste",
		"Turkmenist�n","T�nez","Tonga","Turqu�a","Trinidad y Tobago","Tuvalu","Taiw�n","Ucrania","Uganda","EEUU","Uruguay","Uzbekist�n","Vaticano","San Vicente",
		"Venezuela","I. V�rgenes Brit�nicas","I. V�rgenes EEUU","Vietnam","Vanuatu","Wallis y Futuna","Samoa","Yemen","Mayotte","Sud�frica"}};
}