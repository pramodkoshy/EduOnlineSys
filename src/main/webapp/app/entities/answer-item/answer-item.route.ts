import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAnswerItem, AnswerItem } from 'app/shared/model/answer-item.model';
import { AnswerItemService } from './answer-item.service';
import { AnswerItemComponent } from './answer-item.component';
import { AnswerItemDetailComponent } from './answer-item-detail.component';
import { AnswerItemUpdateComponent } from './answer-item-update.component';

@Injectable({ providedIn: 'root' })
export class AnswerItemResolve implements Resolve<IAnswerItem> {
  constructor(private service: AnswerItemService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAnswerItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((answerItem: HttpResponse<AnswerItem>) => {
          if (answerItem.body) {
            return of(answerItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AnswerItem());
  }
}

export const answerItemRoute: Routes = [
  {
    path: '',
    component: AnswerItemComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'eduOnlineApp.answerItem.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AnswerItemDetailComponent,
    resolve: {
      answerItem: AnswerItemResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.answerItem.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AnswerItemUpdateComponent,
    resolve: {
      answerItem: AnswerItemResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.answerItem.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AnswerItemUpdateComponent,
    resolve: {
      answerItem: AnswerItemResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.answerItem.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
