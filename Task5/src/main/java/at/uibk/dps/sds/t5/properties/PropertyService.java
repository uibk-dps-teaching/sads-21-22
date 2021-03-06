package at.uibk.dps.sds.t5.properties;

import net.sf.opendse.model.Element;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Static method container to access the properties of graph elements.
 * 
 * @author Fedor Smirnov
 *
 */
public class PropertyService extends AbstractPropertyService {

	public enum TaskProperty {
		/**
		 * Secret or not
		 */
		Secret
	}

	public enum ResourceProperty {
		/**
		 * The resource type
		 */
		Type,
		/**
		 * Region where the edge resource is situated.
		 */
		Region
	}

	public enum ResourceType {
		/**
		 * Cloud resource
		 */
		Cloud,
		/**
		 * Edge resource
		 */
		Edge,
		/**
		 * Fog / other type of resource
		 */
		Other
	}

	/**
	 * The properties which are annotated to resources to express the resource's
	 * reliability.
	 */
	public enum ReliabilityProperty {
		Mean, Deviation
	}

	/**
	 * Returns the value of the deviation of the logarithmic reliability of the
	 * given reliability element.
	 * 
	 * @param element the given reliability element
	 */
	public static double getReliabilityDeviation(Element element) {
		checkReliabilityElement(element);
		return (double) getAttribute(element, ReliabilityProperty.Deviation.name());
	}

	/**
	 * Sets the value for the deviation of the logarithmic reliability of the given
	 * element.
	 * 
	 * @param element              the given element
	 * @param reliabilityDeviation the value for the deviation of the logarithmic
	 *                             reliability
	 */
	public static void setReliabilityDeviation(Element element, double reliabilityDeviation) {
		checkReliabilityElement(element);
		element.setAttribute(ReliabilityProperty.Deviation.name(), reliabilityDeviation);
	}

	/**
	 * Returns the average reliability for the given element.
	 * 
	 * @param element the given element
	 * @return the average of the logs of the time to failure
	 */
	public static double getReliabilityAverage(Element element) {
		checkReliabilityElement(element);
		return (double) getAttribute(element, ReliabilityProperty.Mean.name());
	}

	/**
	 * Sets the average reliability value for the given element.
	 * 
	 * @param element            the given reliability element
	 * @param reliabilityAverage the average of the logs of the time to failure
	 */
	public static void setReliabilityAverage(Element element, double reliabilityAverage) {
		checkReliabilityElement(element);
		element.setAttribute(ReliabilityProperty.Mean.name(), reliabilityAverage);
	}

	/**
	 * Checks whether the given element is a reliability element. Throws an
	 * exception if not.
	 * 
	 * @param element the considered element
	 */
	protected static void checkReliabilityElement(Element element) {
		if (!((element instanceof Resource) || (element instanceof Link) || (element instanceof Task))) {
			throw new IllegalArgumentException("The element " + element.getId() + " is not a realibility element.");
		}
	}

	public static boolean isCloud(Resource res) {
		return getResType(res).equals(ResourceType.Cloud);
	}

	public static boolean isEdge(Resource res) {
		return getResType(res).equals(ResourceType.Edge);
	}

	public static void makeEdge(Resource res) {
		setTypeRes(res, ResourceType.Edge);
	}

	public static void makeCloud(Resource res) {
		setTypeRes(res, ResourceType.Cloud);
	}

	/**
	 * Sets the region for the given edge resource.
	 * 
	 * @param res    the given edge resource
	 * @param region the region to set
	 */
	public static void setRegion(Resource res, String region) {
		if (!isEdge(res)) {
			throw new IllegalArgumentException("Resource " + res + " is not an edge resource.");
		} else {
			String attrName = ResourceProperty.Region.name();
			res.setAttribute(attrName, region);
		}
	}

	/**
	 * Returns the region of the given resource (or "unspecified" if the region is
	 * not set).
	 * 
	 * @param res the given resource
	 * @return
	 */
	public static String getRegion(Resource res) {
		String attrName = ResourceProperty.Region.name();
		if (isEdge(res)) {
			return (String) getAttribute(res, attrName);
		} else {
			return "unspecified";
		}
	}

	/**
	 * Sets the type of the given resource.
	 * 
	 * @param res  the given resouce
	 * @param type the type to set
	 */
	protected static void setTypeRes(Resource res, ResourceType type) {
		String attrName = ResourceProperty.Type.name();
		res.setAttribute(attrName, type.name());
	}

	/**
	 * Returns the type of the given resource
	 * 
	 * @param res
	 * @return
	 */
	protected static ResourceType getResType(Resource res) {
		String attrName = ResourceProperty.Type.name();
		if (!isAttributeSet(res, attrName)) {
			return ResourceType.Other;
		}
		return ResourceType.valueOf(res.getAttribute(attrName));
	}

	/**
	 * Returns <code>true</code> if the task is secret and <code>false</code> if it
	 * is not or the parameter is not set.
	 * 
	 * @param t the task to check
	 * @return <code>true</code> if the task is secret and <code>false</code> if it
	 *         is not or the parameter is not set
	 */
	public static boolean isSecret(Task t) {
		String attrName = TaskProperty.Secret.name();
		if (!isAttributeSet(t, attrName)) {
			return false;
		}
		return (boolean) getAttribute(t, attrName);
	}

	/**
	 * Sets the secrecy of the given task
	 * 
	 * @param t      the given task
	 * @param secret true if secret
	 */
	public static void setSecret(Task t, boolean secret) {
		String attrName = TaskProperty.Secret.name();
		t.setAttribute(attrName, secret);
	}
}
