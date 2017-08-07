let state = {};

import { mixin } from 'utils/common.js';

import base from './base.js';
import account from './account.js';
import drawer from './drawer.js';
import anicer from './anicer.js';

mixin(state, base);
mixin(state, account);
mixin(state, drawer);
mixin(state, anicer);

export default state;
