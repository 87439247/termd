/*
 * Copyright 2014 Julien Viet
 *
 * Julien Viet licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */
package io.modsh.core.readline;

import io.modsh.core.telnet.TelnetBootstrap;
import io.modsh.core.telnet.TelnetSession;
import io.modsh.core.telnet.vertx.VertxTelnetBootstrap;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

/**
 * A test class.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReadlineBootstrap {

  public static void main(String[] args) throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    new ReadlineBootstrap("localhost", 4000).start();
    latch.await();
  }

  private final TelnetBootstrap telnet;

  public ReadlineBootstrap(String host, int port) {
    this(new VertxTelnetBootstrap(host, port));
  }

  public ReadlineBootstrap(TelnetBootstrap telnet) {
    this.telnet = telnet;
  }

  public void start() {
    telnet.start(output -> new TelnetSession(output) {

      InputStream inputrc = ReadlineBootstrap.class.getResourceAsStream("inputrc");
      Reader reader = new Reader(inputrc);

      @Override
      public void accept(byte[] data) {
        super.accept(data);
        while (true) {
          Action action = reader.reduceOnce().popKey();
          if (action != null) {
            if (action instanceof KeyAction) {
              KeyAction key = (KeyAction) action;
              System.out.println("Key " + key);
            } else {
              FunctionAction fname = (FunctionAction) action;
              System.out.println("Function " + fname.getName());
            }
          } else {
            break;
          }
        }
      }

      @Override
      protected void onChar(int c) {
        reader.append(c);
      }
    });
  }
}
