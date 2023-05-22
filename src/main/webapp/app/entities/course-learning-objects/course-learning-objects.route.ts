import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICourseLearningObjects, CourseLearningObjects } from 'app/shared/model/course-learning-objects.model';
import { CourseLearningObjectsService } from './course-learning-objects.service';
import { CourseLearningObjectsComponent } from './course-learning-objects.component';
import { CourseLearningObjectsDetailComponent } from './course-learning-objects-detail.component';
import { CourseLearningObjectsUpdateComponent } from './course-learning-objects-update.component';

@Injectable({ providedIn: 'root' })
export class CourseLearningObjectsResolve implements Resolve<ICourseLearningObjects> {
  constructor(private service: CourseLearningObjectsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICourseLearningObjects> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((courseLearningObjects: HttpResponse<CourseLearningObjects>) => {
          if (courseLearningObjects.body) {
            return of(courseLearningObjects.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CourseLearningObjects());
  }
}

export const courseLearningObjectsRoute: Routes = [
  {
    path: '',
    component: CourseLearningObjectsComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'eduOnlineApp.courseLearningObjects.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CourseLearningObjectsDetailComponent,
    resolve: {
      courseLearningObjects: CourseLearningObjectsResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.courseLearningObjects.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CourseLearningObjectsUpdateComponent,
    resolve: {
      courseLearningObjects: CourseLearningObjectsResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.courseLearningObjects.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CourseLearningObjectsUpdateComponent,
    resolve: {
      courseLearningObjects: CourseLearningObjectsResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.courseLearningObjects.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
