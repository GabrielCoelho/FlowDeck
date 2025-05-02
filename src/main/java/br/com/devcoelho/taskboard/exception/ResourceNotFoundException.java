package br.com.devcoelho.taskboard.exception;

/**
 * Exception thrown when a requested resource cannot be found in the system.
 *
 * <p>This exception is typically thrown by repository or service layer methods when attempting to
 * retrieve an entity by its identifier and the entity does not exist in the database.
 *
 * <p>The HTTP status code associated with this exception is 404 Not Found.
 *
 * @author Gabriel Coelho Soares
 * @version 1.0
 * @since 1.0
 */
public class ResourceNotFoundException extends FlowDeckException {

  private final String resourceType;
  private final Object resourceId;

  public ResourceNotFoundException(String resourceType, Object resourceId) {
    super(formatMessage(resourceType, resourceId));
    this.resourceId = resourceId;
    this.resourceType = resourceType;
  }

  private static String formatMessage(String resourceType, Object resourceId) {
    return resourceType + " not found with ID: " + resourceId;
  }

  public String getResourceType() {
    return resourceType;
  }

  public Object getResourceId() {
    return resourceId;
  }
}
