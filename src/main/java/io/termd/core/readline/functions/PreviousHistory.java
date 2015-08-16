/*
 * Copyright 2015 Julien Viet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.termd.core.readline.functions;

import io.termd.core.readline.Function;
import io.termd.core.readline.LineBuffer;
import io.termd.core.readline.Readline;

import java.util.List;

/**
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class PreviousHistory implements Function {

  @Override
  public String name() {
    return "previous-history";
  }

  @Override
  public void apply(Readline.Interaction interaction) {
    List<int[]> history = interaction.history();
    if (history.size() > 0) {
      int curr = interaction.getHistoryIndex();
      int next = curr + 1;
      if (next < history.size()) {
        if (curr == -1) {
          int[] tmp = interaction.buffer().toArray();
          interaction.data().put("abc", tmp);
        }
        int[] line = history.get(next);
        LineBuffer buffer = interaction.buffer();
        buffer.clear();
        buffer.insert(line);
        interaction.setHistoryIndex(next);
      }
    }
  }
}
