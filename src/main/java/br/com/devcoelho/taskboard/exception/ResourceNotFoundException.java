package br.com.devcoelho.taskboard.exception;

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
