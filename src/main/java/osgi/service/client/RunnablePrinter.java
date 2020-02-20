package osgi.service.client;

import osgi.service.printer.Printer;

public class RunnablePrinter implements Runnable {
	private static int counter  = 0;
	private boolean stop;

	private Printer service;

	void setPrinterService(Printer service) {
		this.service = service;
	}

	void start() {
		stop = false;
		new Thread(this).start();
	}

	void stop() {
		stop = true;
	}

	public void run() {
		while (!stop) {
			service.print("hello [" + ++counter + "] ...");
			try {
				Thread.sleep(2000);
				if (counter == 5) {
					counter = 0;
					stop = true;
				}
			} catch (InterruptedException e) {
				stop = true;
			}
		}
	}
}
