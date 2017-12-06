/*
 * Copyright 2015 Rudson Lima
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

package br.liveo.adapter;

public class NavigationLiveoItemAdapter {

	public String title;
	public int counter;
	public int icon;
	public boolean isHeader;
    public int colorSelected = 0;
	public boolean isVisible = true;
	public boolean checked = false;
    public boolean removeSelector = false;

	public NavigationLiveoItemAdapter(String title, int icon, boolean header, int counter, int colorSelected, boolean removeSelector, boolean isVisible) {
		this.title = title;
		this.icon = icon;
		this.isHeader = header;
		this.counter = counter;
		this.isVisible = isVisible;
        this.colorSelected = colorSelected;
        this.removeSelector = removeSelector;
	}

    public NavigationLiveoItemAdapter(String title, int icon, boolean header, int counter) {
        this.title = title;
        this.icon = icon;
        this.isHeader = header;
        this.counter = counter;
		this.isVisible = true;
    }
}
