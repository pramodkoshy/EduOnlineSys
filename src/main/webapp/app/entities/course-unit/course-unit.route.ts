import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICourseUnit, CourseUnit } from 'app/shared/model/course-unit.model';
import { CourseUnitService } from './course-unit.service';
import { CourseUnitComponent } from './course-unit.component';
import { CourseUnitDetailComponent } from './course-unit-detail.component';
import { CourseUnitUpdateComponent } from './course-unit-update.component';

@Injectable({ providedIn: 'root' })
export class CourseUnitResolve implements Resolve<ICourseUnit> {
  constructor(private service: CourseUnitService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICourseUnit> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((courseUnit: HttpResponse<CourseUnit>) => {
          if (courseUnit.body) {
            return of(courseUnit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CourseUnit());
  }
}

export const courseUnitRoute: Routes = [
  {
    path: '',
    component: CourseUnitComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'eduOnlineApp.courseUnit.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CourseUnitDetailComponent,
    resolve: {
      courseUnit: CourseUnitResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.courseUnit.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CourseUnitUpdateComponent,
    resolve: {
      courseUnit: CourseUnitResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.courseUnit.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CourseUnitUpdateComponent,
    resolve: {
      courseUnit: CourseUnitResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.courseUnit.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
