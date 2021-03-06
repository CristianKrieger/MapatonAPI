package mx.krieger.mapaton.publicapi.apis;

import com.google.api.server.spi.config.*;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.common.io.CharStreams;
import mx.krieger.internal.commons.utils.logging.Logger;
import mx.krieger.labplc.mapaton.commons.exceptions.TrailNotFoundException;
import mx.krieger.mapaton.publicutils.handlers.TrailsHandler;
import mx.krieger.mapaton.publicutils.entities.RegisteredTrail;
import mx.krieger.mapaton.publicutils.wrappers.*;
import mx.krieger.mapaton.publicapi.tasks.GtfsGenerationTask;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class is used to manage the contents of the application through the
 * dashboard.
 *
 * @author JJMS (juanjo@krieger.mx)
 * @version 1.0.0.0
 * @since 16 / feb / 2016
 */
@Api(
	name = "mapatonPublicAPI",
	canonicalName = "mapatonPublicAPI",
	title = "Mapaton Public API",
	description = "This is the public API for mapaton",
	authLevel = AuthLevel.NONE,
	namespace = @ApiNamespace( 
		ownerDomain = "mapaton.krieger.mx",
		ownerName = "krieger",
		packagePath = "clients") )
public class MapatonPublicAPI {

	/** The logger. */
	private Logger logger = new Logger(MapatonPublicAPI.class);

	


	/**
	 * Gets a number of trails from the datastore, can be paginated using a cursor (send empty to start from the beginning)
	 *
	 * @author Rodrigo Cabrera (rodrigo.cp@krieger.mx)
	 * @param parameter the cursor to know where to start from, and number of elements needed
	 * @return the number of trails from the cursor sent
	 * @since 16 / feb / 2016
	 */
	@ApiMethod(path = "getAllTrails", name = "getAllTrails", httpMethod = HttpMethod.POST)
	public TrailListResponse getAllTrails(CursorParameter parameter){
		logger.debug("Getting all trails for all user ");

		TrailListResponse result = new TrailsHandler().getAllTrails(parameter);
		logger.debug("All mapped trails ");

		return result;
	}
	
	/**
	 * Gets a number of trails from the datastore, can be paginated using a cursor (send empty to start from the beginning)
	 *
	 * @author Rodrigo Cabrera (rodrigo.cp@krieger.mx)
	 * @param parameter the cursor to know where to start from, and number of elements needed
	 * @return the number of trails from the cursor sent
	 * @since 16 / feb / 2016
	 */
	@ApiMethod(path = "getAllValidTrails", name = "getAllValidTrails", httpMethod = HttpMethod.POST)
	public TrailListResponse getAllValidTrails(CursorParameter parameter){
		logger.debug("Getting all trails for all user ");

		TrailListResponse result = new TrailsHandler().getAllTrails(parameter);
		logger.debug("All mapped trails ");

		return result;
	}
	/**
	 * Gets a number of trails from the datastore, can be paginated using a cursor (send empty to start from the beginning)
	 *
	 * @author Rodrigo Cabrera (rodrigo.cp@krieger.mx)
	 * @param parameter the cursor to know where to start from, and number of elements needed
	 * @return the number of trails from the cursor sent
	 * @since 16 / feb / 2016
	 */
	@ApiMethod(path = "getAllGtfsTrails", name = "getAllGtfsTrails", httpMethod = HttpMethod.POST)
	public TrailListResponse getAllGtfsTrails(CursorParameter parameter){
		logger.debug("Getting all trails for all user ");

		TrailListResponse result = new TrailsHandler().getAllTrails(parameter);
		logger.debug("All mapped trails ");

		return result;
	}


	/**
	 * Gets a specific trail details.
	 *
	 * @author Rodrigo Cabrera (rodrigo.cp@krieger.mx)
	 * @param trailId the trail id
	 * @return the trail details
	 * @throws TrailNotFoundException the trail not found exception
	 * @since 16 / feb / 2016
	 */
	@ApiMethod(name = "getTrailDetails", httpMethod = HttpMethod.POST)
	public TrailDetails getTrailDetails(@Named("trailId") Long trailId) throws TrailNotFoundException{
		logger.debug("Getting details for trail " + trailId);
		RegisteredTrail trail = TrailsHandler.getTrailById(trailId);
		TrailDetails result = new TrailDetails(trail, trail.getCreationDate());
		result.setTotalMeters(trail.getTotalMeters());
		result.setTotalMinutes(trail.getTotalMinutes());
		logger.debug("trail details finished " );
		return result;
	}
	
	


	/**
	 * Gets a number of trails from the datastore, that have a point in an area
	 *
	 * @author Rodrigo Cabrera (rodrigo.cp@krieger.mx)
	 * @param area the cursor to know where to start from, and number of elements needed
	 * @return the number of trails from the cursor sent
	 * @since 5 / 3 / 2016
	 */
	@ApiMethod(path = "trailsNearPoint",name = "trailsNearPoint", httpMethod = HttpMethod.POST)
	public ArrayList<NearTrails> trailsNearPoint(AreaWrapper area){
		logger.debug("Getting stations withing an area: " + area);
		ArrayList<NearTrails> result = new TrailsHandler().trailsNearPoint(area);
		logger.debug("Stations within area finished");
		return result;
	}
	

	/**
	 * Gets a number of trails from the datastore, that go to or from a station
	 *
	 * @author Rodrigo Cabrera (rodrigo.cp@krieger.mx)
	 * @param parameter the keyword for the station name
	 * @return the trails from those stations
	 * @since 5 / 3 / 2016
	 */
	@ApiMethod(name = "getTrailsByStationName", path = "getTrailsByStationName", httpMethod = HttpMethod.POST)
	public ArrayList<TrailDetails> getTrailsByStationName(SearchByKeywordParameter parameter){
		logger.debug("getting stations by keyword: " + parameter);
		ArrayList<TrailDetails> stationsArray = new TrailsHandler().getTrailsByStationName(parameter);
		return stationsArray;
	}
	
	/**
	 * This method gets the list of snapped points for a trail.
	 *
	 * @author Juanjo (juanjo@krieger.mx)
	 * @param parameter the parameter which contains the trail Id, a cursor and the number of elements for pagination
	 * @return the trail snapped points by the google SnapToRoad API
	 * @throws TrailNotFoundException the trail not found exception
	 * @since 18 / Nov / 2015
	 */
	@ApiMethod(path = "getTrailSnappedPoints", name = "getTrailSnappedPoints", httpMethod = HttpMethod.POST)
	public TrailPointsResult getTrailSnappedPoints(TrailPointsRequestParameter parameter) throws TrailNotFoundException {
		logger.debug("Getting snapped points for a trail with parameters: " + parameter);
		TrailPointsResult result = TrailsHandler.getSnappedPointsByTrail(parameter);
		logger.debug("Points of the trail finished ");
		return result;
	}
	
	/**
	 * This method gets the list of raw points for a trail.
	 *
	 * @author Juanjo (juanjo@krieger.mx)
	 * @param parameter the parameter which contains the trail Id, a cursor and the number of elements for pagination
	 * @return the trail raw points as the users registered them
	 * @throws TrailNotFoundException the trail not found exception
	 * @since 11 / Aug / 2015
	 */
	@ApiMethod(path = "getTrailRawPoints", name = "getTrailRawPoints", httpMethod = HttpMethod.POST)
	public TrailPointsResult getTrailRawPoints(TrailPointsRequestParameter parameter) throws TrailNotFoundException {
		logger.debug("Getting raw points for a trail with parameters: " + parameter);
		TrailPointsResult result = new TrailsHandler().getRawPointsByTrail(parameter);
		logger.debug("Points of the trail finished ");
		return result;
	}


	/**
	 * Register the gtfs creation task.
	 *
	 * @author Rodrigo Cabrera (rodrigo.cp@krieger.mx)
	 * @param password a simple password to avoid excess of petitions
	 * @param trailIds the trail ids to be added to the GTFS
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 * @since 19 / feb / 2016
	 */
	@ApiMethod(name = "registerGtfsTask", path = "registerGtfsTask", httpMethod = HttpMethod.POST)
	public void registerGtfsTask(@Named("password") String password, @Named("trailIds") String trailIds) throws TrailNotFoundException, UnsupportedEncodingException, IOException{
		logger.debug("registering gtfs task");
		FileInputStream inputStream = new FileInputStream(new File("WEB-INF/config.txt"));
		String passConfig = CharStreams.toString(new InputStreamReader(inputStream, "UTF-8"));

		if(password.equals(passConfig)){
			HashMap<String, String> params = new HashMap<>();
			params.put(GtfsGenerationTask.Params.trailIds.name(), trailIds);
			
			new GtfsGenerationTask().enqueue(params);
		}
		
		logger.debug("registration finished");
	}
	
	/**
	 * Register the gtfs creation task of all valid trails
	 *
	 * @author Rodrigo Cabrera (rodrigo.cp@krieger.mx)
	 * @param password a simple password to avoid excess of petitions
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 * @since 26 / feb / 2016
	 */
	@ApiMethod(name = "registerGtfsFullTask", path = "registerGtfsFullTask", httpMethod = HttpMethod.POST)
	public void registerGtfsFullTask(@Named("password") String password) throws TrailNotFoundException, UnsupportedEncodingException, IOException{
		logger.debug("registering gtfs task");
		FileInputStream inputStream = new FileInputStream(new File("WEB-INF/config.txt"));
		String passConfig = CharStreams.toString(new InputStreamReader(inputStream, "UTF-8"));

		if(password.equals(passConfig)){
			ArrayList<Long> ids = new TrailsHandler().getAllValidGtfsTrailsIds();
			HashMap<String, String> params = new HashMap<>();
			String trailIds = Arrays.toString(ids.toArray()).replace("[", "").replace("]", "").replace(" ", "");
			params.put(GtfsGenerationTask.Params.trailIds.name(), trailIds);
			
			new GtfsGenerationTask().enqueue(params);
		}
		
		logger.debug("registration finished");
	}
}
