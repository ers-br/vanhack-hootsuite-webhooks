package utils;


/**
 * Interface para informações do sistema.
 */
public class I {

	/** Alias for somethin */
	public static final String ALIAS = "alias";

	/** Version of an item */
	public static final String VERSION = "v";

	/** Index in an ordered list */
	public static final String INDEX = "index";

	/** IP address of a client device */
	public static final String IP = "IP";

	/** list of IP addresses */
	public static final String IPs = "ips";

	/** ID of something */
	public static final String ID = "id";

	/** list of IDs */
	public static final String IDs = "ids";

	/** ID of an item */
	public static final String ITEM_ID = "iID";

	/** an item */
	public static final String ITEM = "item";

	/** some url */
	public static final String URL = "url";

	/** some title */
	public static final String TITLE = "title";

	/** type of an item or property */
	public static final String TYPE = "type";

	/** types of an item or property */
	public static final String TYPES = "types";

	/** A main action */
	public static final String ACTION = "action";

	/** A sub action in another major action context */
	public static final String SUB_ACTION = "subAct";

	/** Indicates an error msg */
	public static final String ERROR = "error";

	/** User email */
	public static final String EMAIL = "email";

	/** Password to be used with email as login */
	public static final String PASS = "pass";

	/** Name of something */
	public static final String NAME = "name";

	/** Name of something, short code */
	public static final String NAME_SHORT = "n";

	/** Real name of something */
	public static final String REAL_NAME = "rName";

	/** Code of something */
	public static final String CODE = "code";

	/** Text/HTML content of something */
	public static final String TEXT = "txt";

	/** Picture of something - usually stored on S3 */
	public static final String PICTURE = "pic";

	/** Set of pictures for something - usually stored on S3 */
	public static final String PICTURES = "pics";

	/** An image - usually stored on S3 */
	public static final String IMAGE = "img";

	/** Set of flags for the item - 'isActive', 'isExpired', etc ... should be used mainly for controlling stuff */
	public static final String FLAGS = "*";

	/** Set of tags for the item - 'Steak', 'Rice', 'Home Delivery', etc ... use it to add simple attributes to the item */
	public static final String TAGS = "#";

	/** Some generic value, as a string */
	public static final String VALUE = "val";

	/** Set of files for something - usually stored on S3 */
	public static final String FILES = "files";

	/** Description of something */
	public static final String DESCRIPTION = "desc";

	/** Status of a response to a remote request */
	public static final String RPC_STATUS = "rpcStatus";

	/** Specifies if an item should NOT be indexed in search engines */
	public static final String HIDDEN = "hidden";

	/** Number of items, objects, etc - type Number */
	public static final String NUMBER = "num";

	// ##############################################################
	// Session

	/** session token */
	public static final String SESSION_KEY = "ssk";

	/** container with all the sessionKeys for the user */
	public static final String SESSION_KEYS_CONTAINER = "ssks";

	/** Especifica qual a aba (frequentemente temos várias abas que compartilham a mesma sessão) */
	public static final String SESSION_TAB_ID = "tabID";

	/** Dados do browser / plataforma / dispositivo) */
	public static final String SESSION_DEVICE_INFO = "device";

	/** Nome do dispositivo - opcional - fornecido pelo usuário */
	public static final String SESSION_DEVICE_NAME = "devName";

	// ##############################################################
	// User

	/** um item de usuário */
	public static final String USER = "user";

	/** User ID ('uID') */
	public static final String USER_ID = "uID";

	/** User ID - same ref. as I.USER_ID */
	public static final String uID = USER_ID;

	/** Alias de URL de um usuário */
	public static final String USER_ALIAS = ALIAS;

	/** CPF de um usuário */
	public static final String USER_CPF = "cpf";

	/** Fixed telephone fixo de um usuário */
	public static final String TELEPHONE = "tel";

	/** Telephone(s), cell phones, skypeID, etc. Text area with free user format. */
	public static final String TELEPHONES = "tels";

	/** Celular do um usuário */
	public static final String CELLPHONE = "cel";

	/** whats-app number */
	public static final String WHATSAPP = "zap";

	/** professional whats-app number */
	public static final String WHATSPRO = "ZAP";

	/** User gender - male / female ... 'M' or 'F' */
	public static final String USER_GENDER = "uG";

	// ##############################################################
	// TIMESTAMPS

	/** Time from UTC in String format */
	public static final String TIME = "t";

	/** Timestamp (ms) from UTC - NUMBER */
	public static final String TIMESTAMP = "time";

	/** ID composed by "{timestamp}_{id-user} - String" */
	public static final String TIME_AND_USER = "t_u";

	/** timestamp when something was updated - number */
	public static final String TIMESTAMP_UPDATED_AT = "uT";

	/** timestamp when something was read (i.e. notification) - number */
	public static final String TIMESTAMP_READ_AT = "rT";

	/** timestamp when something was created - number */
	public static final String TIMESTAMP_CREATED_AT = "cT";

	/** timestamp when something was created - number (same ref. as I.TIMESTAMP_CREATED_AT) */
	public static final String cT = TIMESTAMP_CREATED_AT;

	/** delta time / duration time (the value is a string, and can be in minutes, hours or days). Examples: '30m', '2h', '60d' */
	public static final String TIME_DURATION = "dT";

	/** delta time / duration time (the value is a string, and can be in minutes, hours or days). Examples: '30m', '2h', '60d' */
	public static final String dT = TIME_DURATION;

	// ##############################################################
	// Facebook stuff

	/** Id no facebook, para integração */
	public static final String FB_ID = "fbID";

	/** Token no facebook, para integração */
	public static final String FB_TOKEN = "fbT";

	// ##############################################################
	// Google stuff

	/** ID no google, para integração */
	public static final String GOOGLE_ID = "gID";

	/** Url de imagem do usuário no google, obtida via API call ou durante a autorização inicial desse aplicativo perante o Google */
	public static final String GOOGLE_PICTURE = "gPic";

	/** Token de ID do google, para integração */
	public static final String GOOGLE_ID_TOKEN = "gT";

	// ##############################################################
	// Geolocation

	/** "map" = Map info = String in the format "lat, lon" ... like: "40.715, -74.011" */
	public static final String LAT_LON = "map";

	/** "map" = Map info = String in the format "lat, lon" ... like: "40.715, -74.011" */
	public static final String MAP_INFO = "map";

	/** String in double format, like "40.715" */
	public static final String LATITUDE = "lat";

	/** String in double format, like "-74.011" */
	public static final String LONGITUDE = "lon";

	/** Structured item describing a location (details of an address including map coordinates) */
	public static final String ADDRESS = "addr";

	/** ID that identifies the address in a container (will be the key in that container) */
	public static final String ADDRESS_ID = "addrID";

	/** Addresses container (N internal structured addresses, the values being the addresses - the keys are the 'addrID's) */
	public static final String ADDRESSES = "addrs";

	/** distance, as a number, between two geo-points - usually in Km */
	public static final String DISTANCE = "dist";

	/** minimum something, as a number */
	public static final String MINIMUM = "min";

	/** maximum something, as a number */
	public static final String MAXIMUM = "max";

	/** stock of something (products), as a number */
	public static final String STOCK = "stock";

	/** quantity of something (products), as a number */
	public static final String QUANTITY = "qty";

	/** max distance, as a number, between two geo-points - usually in Km */
	public static final String MAX_DISTANCE = "maxD";

	// ##############################################################
	// Updates

	/** range key for update tables */
	public static final String SEQUENCE = "seq";

	// ##############################################################
	// Money

	/** Currency value (some price), as a number, like '8.5' */
	public static final String PRICE = "R$";

	/** Currency balance, as a number, like '8.5' */
	public static final String BALANCE = "bal";

	/** Currency type, as a string, like 'BRL' or 'USD' [to be used only on multi-currency servers] */
	public static final String CURRENCY = "T$";

	// ##############################################################
	// Like

	public static final String LIKE_STATUS = "LK";

	/** Number of likes for an item */
	public static final String LIKES_YES = "Y";

	/** Number of dislikes for an item */
	public static final String LIKES_NO = "N";

	/** Number of neutral likes for an item */
	public static final String LIKES_NEUTRAL = "YN";

	// ##############################################################
	// Other

	public static final String DATA = "data";
	public static final String INFO = "info";
	public static final String STATUS = "status";

	public static final String CATEGORY = "cat";
	public static final String CATEGORIES = "cats";

	public static final String SPECIAL_TYPES = "spec";

	/** A number to represent some kind of power */
	public static final String POWER = "pow";

	public static final String ORDER = "ord";

	public static final String MESSAGE = "msg";

	/** ordered list of messages */
	public static final String MESSAGES = "msgs";

	/** list of registered mobile devices (installed apps) so that we can 'push' notifications to them */
	public static final String MOBILE_DEVICES = "devs";

	public static final String TOKEN = "tok";

	public static final String ARN = "arn";

	/** a DATE in string format */
	public static final String DATE = "date";

	/** fake userID for when we need to reference the INFO table (example: used in 'uID' in Updates table) */
	public static final String INFO_ID = "*";

	public static final String NODES = "nodes";

	public static final String ATTRIBUTE = "att";

	public static final String CITY = "city";

	public static final String STATE = "state";

	// ----------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------------------

	public static final String SIZE = "size";
	public static final String SIZES = "sizes";

	public static final String SECTION = "section";
	public static final String SECTIONS = "sections";

	public static final String CONTROL_USER_ID_AUTO_INCREMENT = "lastUserID";

	public static final String ADMIN = "admin";

	public static final String NODE = "node";

}

// ----------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------

// ----------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------

// ----------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------

// ----------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------

// ----------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------
