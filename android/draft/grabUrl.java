public class ASUrl extends AccessibilityService {

      @Override
      public void onAccessibilityEvent(AccessibilityEvent event) {

        AccessibilityNodeInfo source = event.getSource();

        if (source == null)
        return;

        final String packageName = String.valueOf(source.getPackageName());

        // Add browser package list here (comma seperated values)
        String BROWSER_LIST = "";

        List<String> browserList 
          = Arrays.asList(BROWSER_LIST.split(",\\s*"));
        if (event.getEventType() 
          == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
          if (!browserList.contains(packageName)) {
            return;
          }
        }

        if (browserList.contains(packageName)) {
          try {
            // App opened is a browser.
            // Parse urls in browser.
            if (AccessibilityEvent
              .eventTypeToString(event.getEventType())
              .contains("WINDOW")) {
              AccessibilityNodeInfo nodeInfo = event.getSource();
              getUrlsFromViews(nodeInfo);
            }
          } catch(StackOverflowError ex){
            ex.printStackTrace();
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        } 
      }

      /**
      * Method to loop through all the views and try to find a URL.
      * @param info
      */
      public void getUrlsFromViews(AccessibilityNodeInfo info) {  

        try {
          if (info == null) 
            return;

          if (info.getText() != null && info.getText().length() > 0) {

            String capturedText = info.getText().toString();

                if (capturedText.contains("https://") 
                  || capturedText.contains("http://")) {
                  if (!previousUrl.equals(capturedText)) {
                    // Do something with the url.
                    previousUrl = capturedText;
                  }
                }
          }

          for (int i = 0; i < info.getChildCount(); i++) {
            AccessibilityNodeInfo child = info.getChild(i);
            getUrlsFromViews(child);
            if(child != null){
              child.recycle();
            }
          }
        } catch(StackOverflowError ex){
          ex.printStackTrace();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }

      @Override
      public void onInterrupt() {

      }
  }