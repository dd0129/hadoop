/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.yarn.server.resourcemanager;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.SystemClock;
import org.apache.hadoop.yarn.api.records.NodeId;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.event.Dispatcher;
import org.apache.hadoop.yarn.event.EventHandler;
import org.apache.hadoop.yarn.server.resourcemanager.rmnode.RMNodeEvent;
import org.apache.hadoop.yarn.server.resourcemanager.rmnode.RMNodeEventType;
import org.apache.hadoop.yarn.util.AbstractLivelinessMonitor;

public class NMLivelinessMonitor extends AbstractLivelinessMonitor<NodeId> {

  private EventHandler dispatcher;
  
  public NMLivelinessMonitor(Dispatcher d) {
    super("NMLivelinessMonitor", new SystemClock());
    this.dispatcher = d.getEventHandler();
  }

  public void init(Configuration conf) {
    super.init(conf);
    setExpireInterval(conf.getInt(YarnConfiguration.RM_NM_EXPIRY_INTERVAL_MS,
        YarnConfiguration.DEFAULT_RM_NM_EXPIRY_INTERVAL_MS));
    setMonitorInterval(conf.getInt(
        YarnConfiguration.RM_NM_LIVENESS_MONITOR_INTERVAL_MS,
        YarnConfiguration.DEFAULT_RM_NM_LIVENESS_MONITOR_INTERVAL_MS));
  }

  @Override
  protected void expire(NodeId id) {
    dispatcher.handle(
        new RMNodeEvent(id, RMNodeEventType.EXPIRE)); 
  }
}