package osgi.service.client;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import osgi.service.printer.Printer;

public class ClientActivator implements BundleActivator, ServiceListener {

	private RunnablePrinter runnablePrinter;
	private BundleContext bundleContext;
	private ServiceReference<Printer> serviceReference;

    @Override
	public void start(BundleContext context) throws Exception {
		runnablePrinter = new RunnablePrinter();
		
		this.bundleContext = context;
		serviceReference = context.getServiceReference(Printer.class);
		if (serviceReference != null) {
			Printer service = context.getService(serviceReference);
			if (service != null) {
				runnablePrinter.setPrinterService(service);
				runnablePrinter.start();
			}
		}
		context.addServiceListener(this, "(objectClass=" + Printer.class.getName() + ")");
    	System.out.println("start : service-client");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
    	if (serviceReference != null) {
			context.ungetService(serviceReference);
		}
    	context.removeServiceListener(this);
        System.out.println("stop : service-client");
    }

    @Override
    public void serviceChanged(ServiceEvent serviceEvent) {
    	switch (serviceEvent.getType()) {
    		case ServiceEvent.UNREGISTERING :
    			runnablePrinter.stop();
    			bundleContext.ungetService(serviceEvent.getServiceReference());
    			break;
    		case ServiceEvent.REGISTERED :
				Printer service = (Printer) bundleContext.getService(
    					                   serviceEvent.getServiceReference());
    			if (service != null) {
    				runnablePrinter.setPrinterService(service);
    				runnablePrinter.start();
    			}
    	}
    }

}
