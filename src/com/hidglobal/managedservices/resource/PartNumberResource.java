package com.hidglobal.managedservices.resource;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import com.hidglobal.managedservices.business.MobileIdsManagement;
import com.hidglobal.managedservices.business.PartNumberManagement;
import com.hidglobal.managedservices.exception.DBException;
import com.hidglobal.managedservices.exception.MAValidationException;
import com.hidglobal.managedservices.utils.ErrorHelper;
import com.hidglobal.managedservices.utils.HIDContants;
import com.hidglobal.managedservices.utils.MAUtils;
import com.hidglobal.managedservices.validator.MAValidator;
import com.hidglobal.managedservices.vo.PartNumber;
import com.hidglobal.managedservices.vo.PartNumbers;
import com.hidglobal.managedservices.vo.QueryFilterId;
import com.hidglobal.managedservices.vo.SearchMobileIdResponse;
import com.hidglobal.managedservices.vo.SearchPartNumberResponse;
import com.hidglobal.managedservices.vo.SearchRequest;
import com.hidglobal.managedservices.vo.Schema.Schemas;

/**
 * 
 * PartNumberResource Class provides interface to query Part Number and Mobile
 * Id informations. URI Path which contains {version}/partnos would be processed
 * by this class. This class is also responsible for setting the Schema
 * Information as per SCIM Standards.
 * 
 */
@Component
@Path("/{id}/part-number")
public class PartNumberResource {

	@Autowired
	private ErrorHelper errorHelper;
	@Autowired
	private MAValidator maValidator;
	@Autowired
	private PartNumberManagement partNumberManagement;
	@Autowired
	private MobileIdsManagement mobileIdsManagement;
	@Autowired
	private MAUtils maUtils;

	@Autowired
	@Qualifier("resourceLocation")
	Properties resourceLocation;
	private static final Logger logger = LoggerFactory
			.getLogger(PartNumberResource.class);

	/**
	 * This method allows consumers to fetch the requested Part Number Details
	 * 
	 * @param companyId
	 *            The Authenticated Company Id
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param partNumberId
	 *            The part number id whose details needs to be fetched.
	 * @return Returns the part number details in PartNumber Object
	 */
	@GET
	@Path("/{partNumberId}")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response getPartNumberDetails(@PathParam("id") long companyId,
			@Context HttpServletRequest request,
			@PathParam("partNumberId") long partNumberId) {

		logger.debug(
				"Performing Operation: Get Part Number. Authenticated CompanyId: [{}]. PartNumber ID: [{}]",
				companyId, partNumberId);
		long startTime = System.currentTimeMillis();
		maUtils.getRequestor(request);
		try {
			QueryFilterId filterByIds = new QueryFilterId();
			filterByIds.setCompanyId(companyId);
			filterByIds.setPartNumberId(partNumberId);

			PartNumber partNumber = partNumberManagement
					.getPartNumberDetails(filterByIds);
			partNumber.getMeta().setLocation(
					MessageFormat.format(partNumber.getMeta().getLocation(),
							new Long(companyId).toString()));
			PartNumbers partNumbers = new PartNumbers();
			List<PartNumber> pn = new ArrayList<PartNumber>();
			pn.add(partNumber);
			partNumbers.setPartNumbers(pn);
			/**
			 * Sets Schema Information
			 */
			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.PART_NUMBER.toString());
			partNumbers.setSchemas(schemas);

			logger.debug("execution Time to Get Part Number Details - [{}]",
					System.currentTimeMillis() - startTime);
			return Response.status(Response.Status.OK).entity(partNumbers).header("Location", partNumber.getMeta().getLocation())
					.build();
		} catch (DBException dbe) {
			logger.error("Part Number [{}] Not Found", dbe.getErrorData());
			Response response = errorHelper.handleException(dbe);
			return response;
		} catch (Exception ex) {

			logger.error(
					"Unexpected Error Happened while Fetching Part Number Details: {[]} ",
					ex);
			Response response = errorHelper.handleException(ex);
			return response;
		}
	}

	/**
	 * This method allows consumers to get the meta information about the
	 * available part numbers associated with the company
	 * 
	 * @param companyId
	 *            The Authenticated Company ID
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @return Returns the meta information of the available part numbers
	 *         associated with the company
	 */
	@GET
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response listPartNumbers(@PathParam("id") long companyId,
			@Context HttpServletRequest request) {

		logger.debug(
				"Performing Operation: List Part Number. Authenticated CompanyId: [{}].",
				companyId);
		long startTime = System.currentTimeMillis();
		maUtils.getRequestor(request);
		try {
			QueryFilterId filterByIds = new QueryFilterId();
			filterByIds.setCompanyId(companyId);
			SearchPartNumberResponse partNumberResponse = partNumberManagement
					.listPartNumbers(filterByIds);
			/**
			 * Sets Schema Information
			 */
			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.LIST_RESPONSE.toString());
			partNumberResponse.setSchemas(schemas);
			String location = MessageFormat.format(
					resourceLocation.getProperty(HIDContants.PARTNUMBERLOCATION),
					new Long(companyId).toString());
			logger.debug("execution Time to List user - [{}]",
					System.currentTimeMillis() - startTime);
			return Response.status(Response.Status.OK)
					.entity(partNumberResponse).header("Location", location).build();
		}

		catch (Exception ex) {
			ex.printStackTrace();
			logger.error(
					"Unexpected Error Happened while Listing Part Number Details:{[]} ",
					ex);
			Response response = errorHelper.handleException(ex);
			return response;
		}

	}

	/**
	 * This method allows consumers to search for the part number information
	 * associated with the company
	 * 
	 * @param companyId
	 *            The Authenticated Company ID
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param searchRequest
	 *            SearchRequest object that has search attributes, search
	 *            filters, count requested by consumer in SCIM Format.
	 * @return Returns the list of Mobile Ids with queried attributes and meta
	 *         information in SCIM format.
	 */

	@POST
	@Path("/.search")
	@Consumes("application/vnd.assaabloy.ma.credential-management-1.0+json")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response searchPartNos(@PathParam("id") long companyId,
			@Context HttpServletRequest request, SearchRequest searchRequest) {
		logger.debug(
				"Performing Operation: Searching Part Number. Authenticated CompanyId: [{}].",
				companyId);
		long startTime = System.currentTimeMillis();
		maUtils.getRequestor(request);
		SearchPartNumberResponse partNoResponse;

		try {

			maValidator.validateSearchRequest(searchRequest,
					Schemas.PART_NUMBER);
            maValidator.validateSearchPartNumberAttribute(searchRequest);
			QueryFilterId filterByIds = new QueryFilterId();
			filterByIds.setCompanyId(companyId);

			partNoResponse = partNumberManagement.searchPartNumber(
					searchRequest, filterByIds);
			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.LIST_RESPONSE.toString());
			partNoResponse.setSchemas(schemas);
			/**
			 * Set Meta Information
			 */
			String location = MessageFormat.format(
					resourceLocation
							.getProperty(HIDContants.PARTNUMBERLOCATION)
							.concat("/.search"), new Long(companyId).toString());
			logger.debug("execution Time to Search Part Number - [{}]",
					System.currentTimeMillis() - startTime);
			return Response.status(Response.Status.OK).entity(partNoResponse)
					.header("Location", location).build();
		} catch (MAValidationException ve) {
			logger.error(
					"Unexpected Error Happened while Searching for PartNumber with Attributes  {[]} ",
					ve);
			Response response = errorHelper.handleException(ve);
			return response;
		}
		catch(DataIntegrityViolationException dive){
			String errorMsg = dive.getCause().getMessage();			
			System.out.println(errorMsg);
			Response response = errorHelper.handleException(new MAValidationException("","Exception while parsing filter," +
					""+errorMsg.substring(errorMsg.indexOf(':')+1, 
					 errorMsg.length()).trim()+".","", 
					 HIDContants.INVALIDFILTER,HIDContants.BADREQUEST));
			return response;		
		}
		catch (Exception ex) {
			logger.error(
					"Unexpected Error Happened while Searching Part Numbers with filters: {[]}, {[]} ",
					searchRequest.getFilterList(), ex);
			Response response = errorHelper.handleException(ex);
			return response;
		}

	}

	/**
	 * This method allows consumers to fetch the TOP N available credentials for
	 * the requested part number. N is defined by Items Per Page.
	 * 
	 * @param companyId
	 *            The Authenticated Company Id
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param partNumberId
	 *            The Part Number Id for which the mobile ids to be listed.
	 * @return
	 */
	@GET
	@Path("/{partNoId}/credential")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response listMobileIds(@PathParam("id") long companyId,
			@Context HttpServletRequest request,
			@PathParam("partNoId") long partNumberId) {
		logger.debug(
				"Performing Operation: List MobileIds for PartNumber. Authenticated CompanyId: [{}]. PartNumber ID: [{}]",
				companyId, partNumberId);
		long startTime = System.currentTimeMillis();
		maUtils.getRequestor(request);
		try {
			QueryFilterId filterByIds = new QueryFilterId();
			filterByIds.setCompanyId(companyId);
			filterByIds.setPartNumberId(partNumberId);
			SearchMobileIdResponse mobileIdResponse = mobileIdsManagement
					.listMobileIds(filterByIds);
			/**
			 * Sets Schema Information
			 */
			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.LIST_RESPONSE.toString());
			mobileIdResponse.setSchemas(schemas);

			/**
			 * Set Meta Information
			 */
			String location=MessageFormat.format(resourceLocation
					.getProperty(HIDContants.PARTNUMBERLOCATION),
					new Long(companyId).toString()).concat("/").concat(String.valueOf(partNumberId)).concat("/credential");
			logger.info(
					"execution Time to List Mobile Ids for Part Number - [{}]",
					System.currentTimeMillis() - startTime);
			return Response.status(HIDContants.SUCCESS)
					.entity(mobileIdResponse).header("Location", location).build();
		}

		catch (Exception ex) {
			ex.printStackTrace();
			logger.error(
					"Unexpected Error Happened while Listing Mobile Ids for Part Number:{[]} ",
					ex);
			Response response = errorHelper.handleException(ex);
			return response;
		}

	}

	/**
	 * This method allows consumers to search Mobile Ids associated with a part
	 * number.
	 * 
	 * @param companyId
	 *            The Authenticated Company ID
	 * @param request
	 *            HTTPServletRequest which has the session attributes.
	 * @param partNumberId
	 *            The part number id for which the mobile ids to be searched.
	 * @param searchRequest
	 *            SearchRequest object that has search attributes, search
	 *            filters, count requested by consumer in SCIM Format.
	 * @return Returns the list of Mobile Ids with queried attributes and meta
	 *         information in SCIM format.
	 */
	@POST
	@Path("/{partNoId}/credential/.search")
	@Consumes("application/vnd.assaabloy.ma.credential-management-1.0+json")
	@Produces("application/vnd.assaabloy.ma.credential-management-1.0+json")
	public Response searchMobileIds(@PathParam("id") long companyId,
			@Context HttpServletRequest request,
			@PathParam("partNoId") long partNumberId,
			SearchRequest searchRequest) {
		logger.debug(
				"Performing Operation: Search MobileId for Part Number. Authenticated CompanyId: [{}]. PartNumber ID: [{}]",
				companyId, partNumberId);
		long startTime = System.currentTimeMillis();
		maUtils.getRequestor(request);
		SearchMobileIdResponse mobileIdResponse;
		/**
		 * Sets Schema Information
		 */
		try {

			maValidator.validateSearchRequest(searchRequest, Schemas.MOBILE_ID);
			maValidator.validateSearchCredentialAttribute(searchRequest);
			QueryFilterId filterByIds = new QueryFilterId();
			filterByIds.setCompanyId(companyId);
			filterByIds.setPartNumberId(partNumberId);
			mobileIdResponse = mobileIdsManagement.searchMobileIds(
					searchRequest, filterByIds);
			List<String> schemas = new ArrayList<String>();
			schemas.add(Schemas.LIST_RESPONSE.toString());
			mobileIdResponse.setSchemas(schemas);
			/**
			 * Set Meta Information
			 */

		} catch (MAValidationException ve) {
			logger.error(
					" Error Happened while Searching for Mobile Ids with Attributes  {[]} ",
					ve);
			Response response = errorHelper.handleException(ve);
			return response;
		}
		catch(DataIntegrityViolationException dive){
			String errorMsg = dive.getCause().getMessage();			
			System.out.println(errorMsg);
			Response response = errorHelper.handleException(new MAValidationException("","Exception while parsing filter," +
					""+errorMsg.substring(errorMsg.indexOf(':')+1, 
					 errorMsg.length()).trim()+".","", 
					 HIDContants.INVALIDFILTER,HIDContants.BADREQUEST));
			return response;		
		}
		catch (Exception ex) {
			logger.error(
					"Unexpected Error Happened while Searching Mobile Ids with filters: {[]}, {[]} ",
					searchRequest.getFilterList(), ex);
			Response response = errorHelper.handleException(ex);
			return response;
		}
		String location = MessageFormat.format(resourceLocation
				.getProperty(HIDContants.PARTNUMBERLOCATION).concat("/"+partNumberId).concat("/credential/.search"),
				new Long(companyId).toString());
		logger.debug(
				"execution Time to Search Mobile Id for Part Number - [{}]",
				System.currentTimeMillis() - startTime);
		return Response.status(Response.Status.OK).entity(mobileIdResponse)
				.header("Location", location).build();
	}

}
